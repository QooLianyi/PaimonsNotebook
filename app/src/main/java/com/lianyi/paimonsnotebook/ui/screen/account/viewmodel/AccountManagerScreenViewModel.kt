package com.lianyi.paimonsnotebook.ui.screen.account.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.components.helper_text.data.HelperTextData
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.enums.HelperTextStatus
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth.AuthClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.BindingClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.screen.login.view.MihoyoLoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountManagerScreenViewModel : ViewModel() {


    private val cookieMap by lazy {
        mutableMapOf<String, String>()
    }

    val helperTextHints by lazy {
        arrayOf(
            CookieHelper.Keys.LToken,
            CookieHelper.Keys.SToken,
            CookieHelper.Keys.CookieToken,
            CookieHelper.Keys.STuid
        ).map {
            HelperTextData("包含${it}", mutableStateOf(HelperTextStatus.Disable)) {
                !cookieMap[it].isNullOrBlank()
            }
        }
    }

    var showMenu by mutableStateOf(false)
    var showAddAccountByCookieDialog by mutableStateOf(false)
    var showAddAccountByWebView by mutableStateOf(false)

    var cookieInputValue by mutableStateOf("")

    lateinit var startActivity: ActivityResultLauncher<Intent>

    var showLoadingDialog by mutableStateOf(false)

    val userList = mutableStateListOf<User>()

    private val bindingClient by lazy {
        BindingClient()
    }

    private val authClient by lazy {
        AuthClient()
    }

    var showConfirmDialog by mutableStateOf(false)

    private var selectedUser: User? = null

    var currentUser: User? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                AccountHelper.userListFlow.collect { list ->
                    userList.clear()
                    userList.addAll(list)
                }
            }
            launch {
                AccountHelper.selectedUserFlow.collect {
                    selectedUser = it
                }
            }
        }
    }

    fun switchUserSelectState(user: User) {
        AccountHelper.apply {
            user.isSelected = !user.isSelected

            if (selectedUser != null && selectedUser!!.userEntity.mid != user.userEntity.mid) {
                selectedUser!!.isSelected = false
                updateUserEntitySelectedState(selectedUser!!, false)
            }

            updateUserEntitySelectedState(user, user.isSelected)
        }
    }


    //检查输入的cookie是否可用，并更新helperText状态,Disable形式
    private fun addAccountCookieValueValidate(): Boolean {
        var available = true
        helperTextHints.forEach {
            available = it.validate.invoke()
            if (available) {
                it.state.value = HelperTextStatus.Available
            } else {
                it.state.value = HelperTextStatus.Disable
            }
        }
        return available
    }

    //提交时,当helperText状态为disable时进行高亮提示,Error形式
    private fun helperTextHint() {
        helperTextHints.forEach {
            if (it.state.value == HelperTextStatus.Disable) {
                it.state.value = HelperTextStatus.Error
            }
        }
    }

    fun onInputTextValueChange(value: String) {
        cookieInputValue = value

        setCookieMap()
        addAccountCookieValueValidate()
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            AccountHelper.deleteUser(user.userEntity)
            "账号[${user.userInfo.nickname}]已删除".notify()
        }
    }

    fun copyCookie(user: User) {
        SystemService.setClipBoardText(user.userEntity.cookies)
        "已将账号[${user.userInfo.nickname}]的cookie复制到剪切板".notify()
    }

    fun showConfirmDialog(user: User) {
        currentUser = user
        showConfirmDialog = true
    }

    fun confirmRefreshDialog() {
        currentUser?.let { refreshUserCookie(it) }
        dismissConfirmDialog()
    }

    fun dismissConfirmDialog() {
        showConfirmDialog = false
    }

    fun showMenu() {
        showMenu = true
    }

    fun dismissMenu() {
        showMenu = false
    }

    fun showLoadingDialog() {
        showLoadingDialog = true
    }

    fun dismissLoadingDialog() {
        showLoadingDialog = false
    }

    fun resetCookieInputDialog() {
        cookieInputValue = ""
        showAddAccountByCookieDialog = false
        helperTextHints.forEach {
            it.state.value =  HelperTextStatus.Disable
        }
    }

    fun showCookieInputDialog() {
        showAddAccountByCookieDialog = true
    }

    fun dismissCookieInputDialog() {
        resetCookieInputDialog()
    }

    private fun refreshUserCookie(user: User) {
        "正在刷新用户的Cookie".notify()
        showLoadingDialog()

        viewModelScope.launch(Dispatchers.IO) {
            val result = AccountHelper.refreshCookieToken(user.userEntity)

            if (result.success) {
                "刷新成功".notify()
            } else {
                "刷新失败:${result.retcode}".errorNotify()
            }
            dismissLoadingDialog()
            currentUser = null
        }
    }

    fun changeUserChosePlayer(user: User, role: UserGameRoleData.Role) {
        "正在修改账号的默认游戏角色".notify()
        showLoadingDialog()

        viewModelScope.launch {
            val authClient = authClient.getActionTicketBySToken(user.userEntity)
            val result = bindingClient.changeGameRoleByDefault(authClient.data.ticket, role)

            if (result.success) {
                AccountHelper.reloadUserGameRoles(user)
                "已将默认游戏角色更改为[${role.nickname}]".notify()
            } else {
                "修改默认游戏角色失败:${result.retcode},${result.message}".warnNotify()
            }
            dismissLoadingDialog()
        }
    }

    private fun setCookieMap() {
        cookieMap.clear()
        cookieMap.putAll(CookieHelper.stringToCookieMap(cookieInputValue))
    }

    fun loadWebView(activity: Activity) {
        startActivity.launch(Intent(activity, MihoyoLoginActivity::class.java))
    }

    fun checkCookie() {
        if (!addAccountCookieValueValidate()) {
            helperTextHint()
            return
        }

        showLoadingDialog()
        dismissCookieInputDialog()

        viewModelScope.launch(Dispatchers.IO) {
            AccountHelper.getUserByCookieMap(cookieMap, onFailed = {
                it.errorNotify()
            }, onSuccess = {
                AccountHelper.addUser(it)
                "账号[${it.mid}]已完成添加".notify()
            })
            dismissLoadingDialog()
            resetCookieInputDialog()
        }
    }

    fun getUserAvatarDiskCacheData(user: User): DiskCache =
        DiskCache(
            url = user.userInfo.avatar_url,
            name = "账户头像",
            createFrom = "账号管理",
            type = DiskCacheDataType.Stable,
            lastUseFrom = "账号管理"
        )

    fun onActivityResult(activityResult: ActivityResult) {
        if (activityResult.data != null) {
            val mid = activityResult.data?.getStringExtra("mid")

            if (mid != null) {
                "账号[$mid]已完成添加".notify()
            }
        }
    }

    fun onBackPressed(onFinished: () -> Unit) {
        if (showMenu || showAddAccountByCookieDialog || showAddAccountByWebView) {
            showMenu = false
            showAddAccountByCookieDialog = false
            showAddAccountByWebView = false
        } else {
            onFinished()
        }
    }

}