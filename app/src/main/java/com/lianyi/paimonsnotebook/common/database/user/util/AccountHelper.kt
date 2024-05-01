package com.lianyi.paimonsnotebook.common.database.user.util

import androidx.compose.runtime.mutableStateListOf
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.data.ResultData
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.user.BbsUserClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.user.UserFullInfoData
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.Cookie
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper.Keys
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper.concatStringToCookie
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

//    private val HasUserFlow = MutableStateFlow(false)
//
//    val hasUserFlow = HasUserFlow.asStateFlow()

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
//                    HasUserFlow.emit(userEntityList.isNotEmpty())
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
                    if (user.isAvailable) {
                        SelectedUser.value = user
                    } else {
                        //如果cookie不可用就将选中状态去掉
                        updateUserEntitySelectedState(userEntity, false)
                    }
                }

                list += user
            }
        } else {
            /*
            * 更新用户列表用户实体部分(userEntity部分)
            * 防止额外的网络请求
            * */
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
    private suspend fun getUserGameRoles(
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
                "账号[${user.aid}]角色信息获取失败:${userGameRoleBySTokenResult.retcode}".errorNotify()
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

    private fun addUser(userEntity: UserEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = getUserByUserEntity(userEntity, true) ?: return@launch

            val currentUserList = UserListFlow.value.toMutableList()

            val sameMidUserIndexOf =
                currentUserList.indexOfFirst { it.userEntity.mid == userEntity.mid }

            //如果列表中没有相同mid的账号就添加到列表中
            if (sameMidUserIndexOf == -1) {
                currentUserList.add(user)
            } else {
                //如果已存在的账号不可用就更新
                val sameMidUser = currentUserList[sameMidUserIndexOf]
                if (!sameMidUser.isAvailable) {
                    currentUserList[sameMidUserIndexOf] = user
                }
            }

            //更新列表与数据库
            UserListFlow.emit(currentUserList)
            dao.insert(userEntity)
        }
    }

    //通过用户实体获取用户信息
    private suspend fun getUserByUserEntity(user: UserEntity, isAdd: Boolean = false): User? {
        val userInfoResult = bbsUserClient.getFullInfo(user)
        val roles = getUserGameRoles(user)

        val userInfo = if (userInfoResult.success) {
            userInfoResult.data.user_info
        } else {
            "账号:[${user.aid}]Cookie失效".errorNotify()

            //当添加用户时，请求必须成功
            if (isAdd) {
                return null
            }

            UserFullInfoData.UserInfo.getEmptyPlaceholder(user.mid)
        }

        return User(
            userEntity = user,
            userInfo = userInfo,
            userGameRoles = mutableStateListOf<UserGameRoleData.Role>().apply {
                addAll(roles)
            },
            isAvailable = userInfoResult.success
        )
    }

    //更新用户选择状态
    fun updateUserEntitySelectedState(user: User, isSelected: Boolean) =
        updateUserEntitySelectedState(user.userEntity, isSelected)

    private fun updateUserEntitySelectedState(user: UserEntity, isSelected: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            val value = if (isSelected) 1 else 0
            dao.updateUserSelectState(value, user.mid)
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

    suspend fun addUserBySToken(
        aid: String,
        mid: String,
        sTokenCookie: Cookie,
        isSelected: Boolean = false
    ): Boolean {

        val cookieTokenRes =
            passportClient.getCookieTokenBySToken(sTokenCookie)

        val ltokenRes = passportClient.getLTokenBySToken(sTokenCookie)

        if (!cookieTokenRes.success) {
            "获取cookie_token失败".errorNotify()
            return false
        }

        val cookieTokenCookie = concatStringToCookie(
            Keys.CookieToken to cookieTokenRes.data.cookie_token,
            Keys.AccountID to aid
        )


        if (!ltokenRes.success) {
            "获取ltoken失败".errorNotify()
            return false
        }

        val lTokenCookie = concatStringToCookie(
            Keys.LToken to ltokenRes.data.ltoken,
            Keys.LTuid to aid
        )

        addUser(
            UserEntity(
                aid = aid,
                mid = mid,
                cookieToken = cookieTokenCookie,
                ltoken = lTokenCookie,
                stoken = sTokenCookie,
                isSelected = isSelected
            )
        )

        return true
    }

    /*
    * 通过cookieMap添加用户
    *
    * */
    suspend fun addUserByCookieMap(
        map: Map<String, String>
    ): String {
        val stoken = map[Keys.SToken] ?: ""
        val stuid = map[Keys.STuid] ?: ""
        val mid = map[Keys.Mid] ?: ""

        val stokenCookie = concatStringToCookie(
            Keys.STuid to stuid,
            Keys.Mid to mid,
            Keys.SToken to stoken
        )
        val addUserSuccess = addUserBySToken(aid = stuid, mid = mid, sTokenCookie = stokenCookie)

        return if (addUserSuccess) stuid else ""
    }
}


