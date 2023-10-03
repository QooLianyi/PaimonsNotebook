package com.lianyi.paimonsnotebook.common.database.user.util

import androidx.compose.runtime.mutableStateListOf
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.data.ResultData
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.user.BbsUserClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.Cookie
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper.Keys
import com.lianyi.paimonsnotebook.common.web.hoyolab.passport.CookieAccountInfoByStokenData
import com.lianyi.paimonsnotebook.common.web.hoyolab.passport.PassportClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth.AuthClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.BindingClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.lianyi.paimonsnotebook.common.database.user.entity.User as UserEntity

/*
* 用户管理
* 提供访问与操作用户列表的方法
* */
object AccountHelper {
    private val bbsUserClient by lazy {
        BbsUserClient()
    }

    private val authClient by lazy {
        AuthClient()
    }

    private val bindingClient by lazy {
        BindingClient()
    }

    private val passportClient by lazy {
        PassportClient()
    }

    //用户数据库实体列表
    private val userEntityList = mutableListOf<UserEntity>()

    private val HasUserFlow = MutableStateFlow(false)

    val hasUserFlow = HasUserFlow.asStateFlow()

    private val dao = PaimonsNotebookDatabase.database.userDao

    private val appWidgetBindingDao by lazy {
        PaimonsNotebookDatabase.database.appWidgetBindingDao
    }

    //用户数据流
    private val UserListFlow = MutableStateFlow<List<User>>(listOf())

    //对外开放的用户数据流
    val userListFlow = UserListFlow.asStateFlow()

    //当前选择的用户流
    private var SelectedUser = MutableStateFlow<User?>(null)

    //对外开放的当前选择用户流
    val selectedUserFlow = SelectedUser.asStateFlow()

    init {
        //接收查询用户实体流
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                //当前选中用户流
                dao.getSelectedUser().collect { userEntity ->
                    SelectedUser.value =
                        UserListFlow.value.takeFirstIf { userEntity?.mid == it.userEntity.mid }
                }
            }
            launch {
                dao.getUserListFlow().collect {
                    userEntityList.clear()
                    userEntityList.addAll(it)
                    HasUserFlow.emit(userEntityList.isNotEmpty())
                    setUserListData()
                }
            }
        }
    }

    //设置用户列表流的值
    private suspend fun setUserListData() {
        val list = mutableListOf<User>()

        //全量刷新
        if (UserListFlow.value.isEmpty()) {
            userEntityList.forEach { userEntity ->
                val user = getUserByUserEntity(userEntity) ?: return@forEach

                if (userEntity.isSelected) {
                    SelectedUser.value = user
                }

                list += user
            }
        } else {
            //更新用户列表用户实体部分
            val map = mutableMapOf<String, UserEntity>()

            userEntityList.forEach { userEntity ->
                map += userEntity.mid to userEntity
            }

            UserListFlow.value.forEach { user ->
                val newUserEntity = map[user.userEntity.mid] ?: user.userEntity
                list += user.copy(userEntity = newUserEntity)
            }
        }

        UserListFlow.emit(list)
    }

    //重新获取指定用户的角色列表
    suspend fun reloadUserGameRoles(user: User) {
        val index = UserListFlow.value.indexOf(user)
        if (index != -1) {
            val roles = getUserGameRoles(user.userEntity)

            user.userGameRoles.apply {
                clear()
                addAll(roles)
            }
        }
    }

    //通过用户实体获取用户角色列表
    suspend fun getUserGameRoles(
        user: UserEntity,
        errorNotify: Boolean = true,
    ): List<UserGameRoleData.Role> {
        val roles = mutableListOf<UserGameRoleData.Role>()

        val userGameRoleBySTokenResult = bindingClient.getUserGameRolesByStoken(user)

        if (userGameRoleBySTokenResult.success) {
            userGameRoleBySTokenResult.data.list.forEach { role ->
                if (role.game_biz == CoreEnvironment.GameBizGenshin) {
                    roles += role
                }
            }
        } else {
            if (errorNotify) {
                "账号[${user.mid}]角色信息获取失败:${userGameRoleBySTokenResult.retcode}".errorNotify()
            }
        }

        return roles
    }

    suspend fun deleteUser(user: UserEntity) {
        val list = UserListFlow.value.toMutableList()
        list.removeIf { it.userEntity.mid == user.mid }

        UserListFlow.value = list

        dao.delete(user)
        //删除绑定的桌面组件
        appWidgetBindingDao.deleteByUserMid(user.mid)
    }

    fun addUser(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = getUserByUserEntity(userEntity) ?: return@launch

            UserListFlow.apply {
                val list = value.toMutableList().apply {
                    if (userListFlow.value.takeFirstIf { it.userEntity.mid == userEntity.mid } == null) {
                        add(user)
                    }
                }
                if (list.size != value.size) {
                    emit(list)
                }
            }
            dao.insert(userEntity)
        }
    }

    //通过用户实体获取用户信息
    private suspend fun getUserByUserEntity(user: UserEntity): User? {
        val userInfoResult = bbsUserClient.getFullInfo(user)
        val roles = getUserGameRoles(user)

        val userInfo = if (userInfoResult.success) {
            userInfoResult.data.user_info
        } else {
            "账号:[${user.mid}]Cookie失效".errorNotify()
            return null
        }

        return User(
            userEntity = user,
            userInfo = userInfo,
            userGameRoles = mutableStateListOf<UserGameRoleData.Role>().apply {
                addAll(roles)
            }
        )
    }

    //更新用户选择状态
    fun updateUserEntitySelectedState(user: User, isSelected: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val value = if (isSelected) 1 else 0
            dao.updateUserSelectState(value, user.userEntity.mid)
        }
    }

    suspend fun refreshCookieToken(user: UserEntity): ResultData<CookieAccountInfoByStokenData> {
        val result = passportClient.getCookieTokenBySToken(user.stoken)
        if (result.success) {
            val data = result.data

            addUser(
                user.copy(cookieToken = Cookie().apply {
                    this[Keys.CookieToken] = data.cookie_token
                    this[Keys.AccountID] = data.uid
                })
            )
        }
        return result
    }

    //通过stoken,stuid,获取账号
    suspend fun getUserByCookieMap(
        map: Map<String, String>,
        onFailed: (String) -> Unit,
        onSuccess: (UserEntity) -> Unit,
    ) {
        val stoken = map[Keys.SToken] ?: ""
        val stuid = map[Keys.STuid] ?: ""

        if (stoken.isBlank() || stuid.isBlank()) {
            onFailed("stoken或stuid为空")
            return
        }

        val mid = map[Keys.Mid] ?: ""

        val getSTokenByOldTokenResult =
            passportClient.accountGetSTokenByOldToken(stoken, stuid, mid)

        if (!getSTokenByOldTokenResult.success) {
            onFailed("添加账号时出现错误:${if (getSTokenByOldTokenResult.retcode == -100) "stoken或mid参数错误,请检查后重新尝试" else ""}")
            return
        }

        val stokenCookie = Cookie().apply {
            this[Keys.SToken] = getSTokenByOldTokenResult.data.token.token
            this[Keys.Mid] = getSTokenByOldTokenResult.data.user_info.mid
            this[Keys.STuid] = stuid
        }

        val getLTokenBySTokenResult = passportClient.getLTokenBySToken(stokenCookie)

        if (!getLTokenBySTokenResult.success) {
            onFailed("获取时出现错误:ltoken-${getLTokenBySTokenResult.retcode}")
            return
        }

        val ltokenCookie = Cookie().apply {
            this[Keys.LToken] = getLTokenBySTokenResult.data.ltoken
            this[Keys.LTuid] = getSTokenByOldTokenResult.data.user_info.aid
        }

        val getCookieTokenBySTokenResult = passportClient.getCookieTokenBySToken(stokenCookie)
        if (!getCookieTokenBySTokenResult.success) {
            onFailed("获取时出现错误:cookieToken-${getCookieTokenBySTokenResult.retcode}")
            return
        }

        val cookieTokenCookie = Cookie().apply {
            this[Keys.CookieToken] = getCookieTokenBySTokenResult.data.cookie_token
            this[Keys.AccountID] = getCookieTokenBySTokenResult.data.uid
        }

        onSuccess(
            UserEntity(
                aid = getSTokenByOldTokenResult.data.user_info.aid,
                mid = getSTokenByOldTokenResult.data.user_info.mid,
                cookieToken = cookieTokenCookie,
                ltoken = ltokenCookie,
                stoken = stokenCookie,
                isSelected = selectedUserFlow.value == null //当没有选中的用户时自动设置为选中用户
            )
        )

    }

}


