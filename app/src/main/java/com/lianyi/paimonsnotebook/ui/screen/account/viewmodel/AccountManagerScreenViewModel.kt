package com.lianyi.paimonsnotebook.ui.screen.account.viewmodel

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geetest.sdk.GT3ConfigBean
import com.geetest.sdk.GT3GeetestUtils
import com.geetest.sdk.utils.GT3ServiceNode
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.lianyi.paimonsnotebook.common.components.helper_text.data.HelperTextData
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.scope.withContextMain
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.takeValueFromUrl
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.enums.HelperTextStatus
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import com.lianyi.paimonsnotebook.common.util.system_service.sdkVersionLessThanOrEqualTo29
import com.lianyi.paimonsnotebook.common.web.hoyolab.api_sdk.combo_panda.QRCodeClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.passport.PassportClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.passport.XRpcAigisData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth.AuthClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.BindingClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.screen.account.data.LoginByCaptchaCache
import com.lianyi.paimonsnotebook.ui.screen.account.util.CaptchaCallbackType
import com.lianyi.paimonsnotebook.ui.screen.account.util.GT3ListenerImpl
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class AccountManagerScreenViewModel : ViewModel() {

    val userList = mutableStateListOf<User>()

    private var selectedUser: User? = null

    init {
        viewModelScope.launchIO {
            //在主线程等待state对象创建完毕
            launchIO {
                AccountHelper.userListFlow.collect { list ->
                    withContextMain {
                        userList.clear()
                        userList.addAll(list)
                    }
                }
            }
            launchIO {
                AccountHelper.selectedUserFlow.collect {
                    withContextMain {
                        selectedUser = it
                    }
                }
            }
        }
    }

    //登录二维码bitmap
    var loginQrCodeBitmap: Bitmap? by mutableStateOf(null)
        private set

    //显示二维码popup
    var showQRCodePopup by mutableStateOf(false)
        private set

    private val cookieMap by lazy {
        mutableMapOf<String, String>()
    }

    val helperTextHints by lazy {
        arrayOf(
            CookieHelper.Keys.STuid,
            CookieHelper.Keys.SToken,
            CookieHelper.Keys.Mid
        ).map {
            HelperTextData("包含${it}", mutableStateOf(HelperTextStatus.Disable)) {
                !cookieMap[it].isNullOrBlank()
            }
        }
    }

    //待操作用户
    var pendingActionUser: User? = null

    var showMenu by mutableStateOf(false)

    var showRefreshCookieConfirmDialog by mutableStateOf(false)
        private set

    //cookie输入对话框
    var showAddAccountByCookieDialog by mutableStateOf(false)
        private set

    //确认删除用户对话框
    var showConfirmDeleteUserDialog by mutableStateOf(false)
        private set

    //手机号输入对话框
    var showPhoneNumberInputDialog by mutableStateOf(false)
        private set

    //验证码输入对话框
    var showLoginCaptchaCodeInputDialog by mutableStateOf(false)
        private set

    var cookieInputValue by mutableStateOf("")
        private set

    var showLoadingDialog by mutableStateOf(false)
        private set

    lateinit var startActivity: ActivityResultLauncher<Intent>

    lateinit var requestStoragePermission: () -> Unit

    lateinit var checkStoragePermission: () -> Boolean


    private val bindingClient by lazy {
        BindingClient()
    }

    private val authClient by lazy {
        AuthClient()
    }

    private val passportClient by lazy {
        PassportClient()
    }

    private val qrCodeClient by lazy {
        QRCodeClient()
    }

    lateinit var getGeeTestUtils: () -> GT3GeetestUtils

    fun showConfirmDialog(user: User) {
        pendingActionUser = user
        showRefreshCookieConfirmDialog = true
    }

    fun confirmRefreshCookieDialog() {
        val user = pendingActionUser
        if (user == null) {
            "刷新cookie失败:选中用户为空".errorNotify()
            return
        }

        refreshUserCookie(user)
        dismissConfirmDialog()
    }

    fun dismissConfirmDialog() {
        showRefreshCookieConfirmDialog = false
    }

    fun toggleMenu() {
        showMenu = !showMenu
    }

    fun dismissMenu() {
        showMenu = false
    }

    private fun showLoadingDialog() {
        showLoadingDialog = true
    }

    private fun dismissLoadingDialog() {
        showLoadingDialog = false
    }

    fun showCookieInputDialog() {
        showAddAccountByCookieDialog = true
    }

    fun dismissCookieInputDialog() {
        resetCookieInputDialog()
    }

    fun showConfirmDeleteUserDialog() {
        showConfirmDeleteUserDialog = true
    }

    fun dismissConfirmDeleteUserDialog() {
        showConfirmDeleteUserDialog = false
    }

    fun showPhoneNumberInputDialog() {
        showPhoneNumberInputDialog = true
    }

    fun dismissPhoneNumberInputDialog() {
        showPhoneNumberInputDialog = false
    }

    fun showLoginCaptchaCodeInputDialog() {
        showLoginCaptchaCodeInputDialog = true
    }

    fun dismissLoginCaptchaCodeInputDialog() {
        showLoginCaptchaCodeInputDialog = false
    }


    //切换用户选择状态
    fun switchUserSelectState(user: User) {
        if (!user.isAvailable) {
            "账号[${user.userEntity.mid}]Cookie失效,请重新登录".warnNotify(false)
            return
        }

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

    fun deleteUser() {
        val user = pendingActionUser

        if (user == null) {
            "待删除用户为空".errorNotify()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            AccountHelper.deleteUser(user.userEntity)

            pendingActionUser = null
            dismissConfirmDeleteUserDialog()

            "账号[${user.userInfo.nickname}]已删除".notify()
        }
    }

    fun onDeleteUser(user: User) {
        pendingActionUser = user
        showConfirmDeleteUserDialog()
    }

    fun copyCookie(user: User) {
        SystemService.setClipBoardText(user.userEntity.cookies)
        "已将账号[${user.userInfo.nickname}]的cookie复制到剪切板".notify()
    }

    private fun resetCookieInputDialog() {
        cookieInputValue = ""
        showAddAccountByCookieDialog = false
        helperTextHints.forEach {
            it.state.value = HelperTextStatus.Disable
        }
    }

    private fun refreshUserCookie(user: User) {
        "正在刷新[${user.userInfo.nickname}]的Cookie".notify()
        showLoadingDialog()

        viewModelScope.launch(Dispatchers.IO) {
            val result = AccountHelper.refreshCookieToken(user.userEntity)

            if (result.success) {
                "刷新成功".notify()
            } else {
                "刷新失败:${result.retcode}".errorNotify()
            }
            dismissLoadingDialog()
            pendingActionUser = null
        }
    }

    fun changeUserChosePlayer(user: User, role: UserGameRoleData.Role) {
        val chooseRole = user.userGameRoles.takeFirstIf { it.is_chosen }

        //已经选中的用户不能再设置为选中
        if (chooseRole != null && chooseRole.game_uid == role.game_uid) {
            return
        }

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

    fun checkCookie() {
        if (!addAccountCookieValueValidate()) {
            helperTextHint()
            return
        }

        showLoadingDialog()
        dismissCookieInputDialog()

        viewModelScope.launch(Dispatchers.IO) {
            val stuid = AccountHelper.addUserByCookieMap(cookieMap)

            if (stuid.isNotEmpty()) {
                "账号[${stuid}]添加完毕".notify()
            }

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

    fun onBackPressed(onFinished: () -> Unit) {
        if (showMenu || showAddAccountByCookieDialog) {
            showMenu = false
            showAddAccountByCookieDialog = false
        } else {
            onFinished()
        }
    }

    fun onRequestQRCodePopupDismiss() {
        showQRCodePopup = false
    }

    fun showQRCodePopup() {
        showQRCodePopup = true

        viewModelScope.launchIO {
            val res = qrCodeClient.fetch()
            if (!res.success) {
                "获取二维码失败:${res.message}".errorNotify()
                return@launchIO
            }
            val ticket = res.data.url.takeValueFromUrl("ticket")

            if (ticket.isEmpty()) {
                "没有获取到ticket".errorNotify()
                return@launchIO
            }
            loginQrCodeBitmap = createQrCode(res.data.url, 200)

            loopQueryQrCodeState(ticket)
        }
    }

    //循环检查二维码状态
    private suspend fun loopQueryQrCodeState(ticket: String) {
        while (true) {
            delay(3000)

            val queryRes = qrCodeClient.query(ticket)

            //请求不成功时处理,可能是二维码失效
            if (!queryRes.success) {
                "二维码已失效".errorNotify()
                return
            }

            //未完成登录
            if (!queryRes.data.payload.success) continue

            val raw = queryRes.data.payload.getRawData()
            getCookieByGameToken(raw.uid, raw.token)

            return
        }
    }

    //通过gameToken获取cookie
    private suspend fun getCookieByGameToken(uid: String, token: String) {
        val uidNumber = uid.toIntOrNull() ?: 0
        val sTokenRes = passportClient.getTokenByGameToken(uidNumber, token)

        if (!sTokenRes.success) {
            "获取stoken失败".errorNotify()
            return
        }

        addUserBySTokenString(
            sTokenRes.data.token.token,
            sTokenRes.data.user_info.mid,
            sTokenRes.data.user_info.aid,
        )

        onRequestQRCodePopupDismiss()
        loginQrCodeBitmap = null
    }

    //前往米游社个人界面
    fun goHoyolabSelfPage(saveQRCodeToLocal: Boolean) {
        if (saveQRCodeToLocal) {
            saveLoginQrCodeToLocal()
        }

        try {
            HomeHelper.goActivityByIntentNewTask {
                component = ComponentName(
                    "com.mihoyo.hyperion",
                    "com.mihoyo.hyperion.main.HyperionMainActivity"
                )
                putExtra("home_index_key", "PAGE_MYSELF")
            }
        } catch (_: ActivityNotFoundException) {
            "你还没有安装米游社".errorNotify()
        } catch (e: Exception) {
            "发生未知错误".errorNotify()
        }
    }

    //保存二维码到本地
    private fun saveLoginQrCodeToLocal() {
        if (loginQrCodeBitmap == null) {
            "没有找到登录二维码".errorNotify()
            return
        }

        sdkVersionLessThanOrEqualTo29(
            finally = {
                FileHelper.saveTempImage(loginQrCodeBitmap!!)
            }
        ) {
            if (!FileHelper.hasWriteExternalStorage) {
                return@sdkVersionLessThanOrEqualTo29
            }
        }
    }

    /*
    * 从zxing库中抽离的方法,固定生成无边距、黑色的二维码
    * */
    private fun createQrCode(content: String, size: Int): Bitmap? = try {
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "utf-8",
            EncodeHintType.MARGIN to 0 /* default = 4 */
        )

        val bitMatrix =
            QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, size, size, hints)

        val pixels = IntArray(size * size)
        // 下面这里按照二维码的算法，逐个生成二维码的图片，
        // 两个for循环是图片横列扫描的结果
        for (y in 0 until size) {
            for (x in 0 until size) {
                pixels[y * size + x] = if (bitMatrix[x, y]) Color.BLACK else Color.WHITE
            }
        }

        // 生成二维码图片的格式
        Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
            setPixels(pixels, 0, size, 0, 0, size, size)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    private suspend fun addUserBySTokenString(
        sToken: String,
        mid: String,
        aid: String
    ) {
        val sTokenCookie = CookieHelper.concatStringToCookie(
            CookieHelper.Keys.SToken to sToken,
            CookieHelper.Keys.Mid to mid,
            CookieHelper.Keys.STuid to aid,
        )

        //通过st添加账号,如果用户列表为空就设置为默认角色
        val addUserSuccess = AccountHelper.addUserBySToken(
            aid = aid,
            mid = mid,
            sTokenCookie = sTokenCookie,
            isSelected = userList.isEmpty() || selectedUser?.userEntity?.mid == mid
        )

        if (addUserSuccess) {
            "账号[${aid}]添加完毕".notify()
        }
    }

    private var geetestUtils: GT3GeetestUtils? = null

    private suspend fun configAndShowGeeTestCaptcha(
        rawAigis: String,
        callbackType: CaptchaCallbackType
    ) {
        if (!this::getGeeTestUtils.isInitialized) {
            "登录失败:方法未初始化".errorNotify()
            return
        }

        val aigisData = JSON.parse<XRpcAigisData>(rawAigis)

        geetestUtils = getGeeTestUtils.invoke()

        val config = GT3ConfigBean().apply {
            pattern = 1
            isCanceledOnTouchOutside = false
            lang = "zh-cn"
            timeout = 10000
            webviewTimeout = 10000
            gt3ServiceNode = GT3ServiceNode.NODE_IPV6

            listener = GT3ListenerImpl(
                onButtonClick = {
                    this.api1Json = JSONObject().apply {
                        put("gt", aigisData.data.gt)
                        put("challenge", aigisData.data.challenge)
                        put("success", aigisData.data.success)
                        put("new_captcha", aigisData.data.new_captcha)
                    }
                    geetestUtils?.getGeetest()
                },
                onDialogResult = {
                    val aigis =
                        "${aigisData.session_id};${Base64.encodeToString(it.toByteArray(), 2)}"
                    when (callbackType) {
                        CaptchaCallbackType.LoginByCaptcha -> {
                            loginByMobileCaptcha(captchaCache.code, aigis)
                        }

                        CaptchaCallbackType.CreateCaptcha -> {
                            createLoginCaptcha(captchaCache.mobile, aigis)
                        }
                    }

                    geetestUtils?.destory()
                    geetestUtils = null
                }
            )
        }

        geetestUtils?.init(config)

        dismissPhoneNumberInputDialog()
        dismissLoginCaptchaCodeInputDialog()

        withContextMain {
            geetestUtils?.startCustomFlow()
        }
    }

    private val captchaCache by lazy {
        LoginByCaptchaCache()
    }

    private suspend fun onNeedCaptcha(
        headers: List<Pair<String, String>>?,
        callbackType: CaptchaCallbackType
    ) {
        if (headers == null) {
            "需要进行验证,但没有获取到验证内容".warnNotify()

            dismissLoginCaptchaCodeInputDialog()
            dismissPhoneNumberInputDialog()
            return
        }
        val rawAigis = headers.takeFirstIf { it.first == "X-Rpc-Aigis" }

        if (rawAigis == null) {
            "需要进行验证,但没有获取到验证内容".errorNotify()
            return
        }

        configAndShowGeeTestCaptcha(
            rawAigis.second,
            callbackType
        )
    }

    fun createLoginCaptcha(mobile: String, aigis: String = "") {
        if (mobile.length != 11) {
            "手机号必须为11位".warnNotify(false)
            return
        }

        captchaCache.mobile = mobile

        viewModelScope.launchIO {
            val res =
                passportClient.createLoginCaptcha(
                    mobile = captchaCache.mobile,
                    areaCode = captchaCache.areaCode,
                    aigis = aigis
                )

            if (res.success) {
                captchaCache.actionType = res.data.action_type

                showLoginCaptchaCodeInputDialog()
                dismissPhoneNumberInputDialog()

                "验证码已发送".notify()
            } else if (res.needAigis) {
                onNeedCaptcha(res.responseHeaders, CaptchaCallbackType.CreateCaptcha)
            } else {
                "发送验证码失败:${res.message}".errorNotify()
            }
        }
    }

    fun loginByMobileCaptcha(code: String, aigis: String = "") {
        captchaCache.code = code
        viewModelScope.launchIO {
            val res = passportClient.loginByMobileCaptcha(
                actionType = captchaCache.actionType,
                mobile = captchaCache.mobile,
                areaCode = captchaCache.areaCode,
                code = code,
                aigis = aigis
            )

            if (res.success) {
                addUserBySTokenString(
                    sToken = res.data.token.token,
                    mid = res.data.user_info.mid,
                    aid = res.data.user_info.aid
                )

                dismissLoginCaptchaCodeInputDialog()
            } else if (res.needAigis) {
                onNeedCaptcha(res.responseHeaders, CaptchaCallbackType.LoginByCaptcha)
            } else {
                "登录失败:${res.message}".errorNotify()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        geetestUtils?.destory()
        geetestUtils = null
    }
}