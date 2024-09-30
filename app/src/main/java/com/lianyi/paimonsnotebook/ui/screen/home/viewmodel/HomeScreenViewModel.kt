package com.lianyi.paimonsnotebook.ui.screen.home.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.DrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.king.camera.scan.CameraScan
import com.lianyi.paimonsnotebook.common.data.hoyolab.game_record.DailyNote
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.daily_note.util.DailyNoteHelper
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.extension.intent.setRequestCode
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.service.overlay.util.OverlayHelper
import com.lianyi.paimonsnotebook.common.util.activity_code.ActivityCode
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirst
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.view.HoyolabWebActivity
import com.lianyi.paimonsnotebook.common.view.QRCodeScanActivity
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.WebHomeClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.NearActivityData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.OfficialRecommendedPostsData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.WebHomeData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData
import com.lianyi.paimonsnotebook.ui.screen.home.service.HoyolabQRCodeService
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostHelper
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostType
import com.lianyi.paimonsnotebook.ui.screen.home.view.PostDetailScreen
import com.lianyi.paimonsnotebook.ui.screen.setting.data.ConfigurationData
import com.lianyi.paimonsnotebook.ui.screen.setting.util.SettingsHelper
import com.lianyi.paimonsnotebook.ui.screen.setting.util.UpdateService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {

    //配置
    var configurationData by mutableStateOf(ConfigurationData())

    //当前选择的用户
    var selectedUser by mutableStateOf<User?>(null)

    //祈愿记录列表
    val dailyNoteList = mutableStateListOf<DailyNote>()

    var showConfirm by mutableStateOf(false)

    private val updateService = UpdateService()

    val modalItems = mutableStateListOf<ModalItemData>()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                SettingsHelper.configurationFlow.collect {
                    configurationData = it
                    checkOverlayPermission()
                }
            }
            launch {
                HomeHelper.modalItemsFlow.collect {
                    modalItems.clear()
                    modalItems += it
                }
            }
            launch {
                AccountHelper.selectedUserFlow.collect {
                    selectedUser = it
                }
            }
            launch {
                DailyNoteHelper.dailyNoteFlow.collect {
                    dailyNoteList.clear()
                    dailyNoteList.addAll(it)
                }
            }
            launch {
                dataStoreValuesFirst {
                    val check = it[PreferenceKeys.EnableCheckNewVersion] ?: true
                    if (check) {
                        updateService.checkNewVersion(onFoundNewVersion = {
                            "发现新版本,可前往设置进行更新[关于->派蒙笔记本]".notify(closeable = true)
                        }, onFail = {
                        }, onNotFoundNewVersion = {
                        })
                    }
                }
            }
            launch {
                if (configurationData.enableMetadata) {
                    MetadataHelper.checkAndUpdateMetadata()
                }
            }
        }
    }

    //二维码服务
    private val qrCodeService by lazy {
        HoyolabQRCodeService()
    }

    var showUserDialog by mutableStateOf(false)

    private val webHomeClient by lazy {
        WebHomeClient()
    }

    //轮播图列表
    val bannerList = mutableStateListOf<WebHomeData.Carousel>()

    lateinit var startActivity: ActivityResultLauncher<Intent>

    val nearActivity = mutableStateListOf<NearActivityData.Hots.Group2.Children.NearActivity>()

    val noticeList = mutableStateListOf<OfficialRecommendedPostsData.OfficialRecommendedPost>()
    var noticeStatus by mutableStateOf(LoadingState.Empty)

    var isRefreshing by mutableStateOf(false)

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            getWebHome()
            getOfficialRecommendedPosts()
            getNearActivity()
        }
    }

    private suspend fun getWebHome() {
        webHomeClient.getWebHome().apply {
            if (success) {
                bannerList.clear()
                bannerList.addAll(data.carousels)
            } else {
                "轮播图请求失败:${retcode}".errorNotify()
            }
        }
    }

    //加载公告并设置对应的加载状态
    private suspend fun getOfficialRecommendedPosts() {
        noticeStatus = LoadingState.Loading
        webHomeClient.getOfficialRecommendedPosts().apply {
            if (success) {
                noticeList.clear()
                noticeList.addAll(data.list)
            } else {
                "公告列表请求失败:${retcode}".errorNotify()
            }
        }
    }

    fun dismissUserDialog() {
        showUserDialog = false
    }

    fun showUserDialog() {
        showUserDialog = true
    }

    //获取近期活动
    private suspend fun getNearActivity() {
        webHomeClient.getNearActivity().apply {
            if (success && data.list.isNotEmpty()) {
                nearActivity.clear()
                data.list.forEach { hots ->
                    if (hots.id == 48) {
                        hots.children.forEach { group ->
                            if (group.id == 52) {
                                group.children.forEach { child ->
                                    if (child.id == 53) {
                                        nearActivity.addAll(child.list)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                "近期活动请求失败:${retcode}".errorNotify()
            }
        }
    }

    //文章跳转
    fun goPostDetail(
        postId: String,
        postType: PostType = PostType.Default,
    ) {
        HomeHelper.goActivityByIntentNewTask {
            when (postType) {
                PostType.Default, PostType.Notice -> {
                    putExtra(PostHelper.PARAM_POST_ID, getArticleIdFromUrl(postId))
                    setComponentName(PostDetailScreen::class.java)
                }

                PostType.Banner, PostType.ActivityCalendar -> {
                    //判断是否为网页
                    val tag = "/article/"
                    if (postId.contains(tag)) {
                        putExtra(PostHelper.PARAM_POST_ID, getArticleIdFromUrl(postId))
                        setComponentName(PostDetailScreen::class.java)
                    } else {
                        //网页时，帖子id为网页url
                        setComponentName(HoyolabWebActivity::class.java)
                        putExtra(HoyolabWebActivity.EXTRA_URL, postId)
                    }
                }
            }

        }
    }

    //转换文章的ID
    private fun getArticleIdFromUrl(url: String): Long {
        val articlePosition = url.lastIndexOf("/") + 1
        val urlLength = url.length
        val takeCount = urlLength - articlePosition
        return url.takeLast(takeCount).split("?").first().toLongOrNull() ?: 0L
    }

    fun functionNavigate(cls: Class<out Activity>) {
        HomeHelper.goActivity(cls)
    }

    private fun checkOverlayPermission() {
        if (configurationData.enableOverlay) {
            showConfirm = !OverlayHelper.checkPermission()
        }
    }

    fun requestOverlayPermission() {
        if (this::startActivity.isInitialized) {
            OverlayHelper.requestPermission(startActivity)
        }
    }

    fun removeOverlayPermissionFlag() {
        configurationData.enableOverlay = false
        showConfirm = false
        viewModelScope.launch {
            PreferenceKeys.EnableOverlay.editValue(false)
        }
        "缺少权限,已关闭所有悬浮窗".warnNotify(false)
    }

    fun refreshData() {
        isRefreshing = true

        "正在获取最新数据".notify()

        viewModelScope.launch(Dispatchers.IO) {
            listOf(
                async { getWebHome() },
                async { getOfficialRecommendedPosts() },
                async { getNearActivity() }
            ).joinAll()

            isRefreshing = false

            launch(Dispatchers.Main) {
                "已获取最新数据".notify()
            }
        }
    }

    fun toggleModalDrawer(modalState: DrawerState, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            if (modalState.isClosed) {
                modalState.open()
            } else {
                modalState.close()
            }
        }
    }

    //处理返回事件
    fun onBackPressed(onBackPressed: () -> Unit) {
        onBackPressed()
    }

    //处理activity返回的数据
    fun onActivityResult(activityResult: ActivityResult) {
        when (activityResult.resultCode) {
            0 -> {
                checkOverlayPermission()
                if (showConfirm) {
                    removeOverlayPermissionFlag()
                }
            }

            Activity.RESULT_OK -> {
                val activityResultCode =
                    activityResult.data?.getIntExtra(ActivityCode.EXTRA_RESULT_CODE, 0) ?: 0
                if (activityResultCode == ActivityCode.QRCodeScanActivity) {
                    scanQRCode(activityResult.data?.getStringExtra(CameraScan.SCAN_RESULT))
                }
            }
        }
    }

    //用户对话框选择完毕回调标记
    private var forGoSignWeb = false

    fun goSignWeb(user: User? = null) {
        val targetUser = user
            ?: if (configurationData.alwaysUseDefaultUser && selectedUser != null) {
                selectedUser!!
            } else {
                forGoSignWeb = true
                showUserDialog()
                return
            }

        HomeHelper.goActivityForResultByIntent(startActivity) {
            setComponentName(HoyolabWebActivity::class.java)
            putExtra(HoyolabWebActivity.EXTRA_URL, ApiEndpoints.GenshinSign)
            putExtra(HoyolabWebActivity.EXTRA_MID, targetUser.userEntity.mid)
            setRequestCode(ActivityCode.HomeScreen)
        }

        dismissUserDialog()
    }

    fun onScanQRCode() {
        HomeHelper.goActivityForResultByIntent(startActivity) {
            setComponentName(QRCodeScanActivity::class.java)
            setRequestCode(ActivityCode.HomeScreen)
        }
    }

    private fun scanQRCode(qrCodeContent: String?) {
        if (qrCodeContent == null) {
            "没有获取到二维码的信息".errorNotify()
            return
        }

        viewModelScope.launchIO {
            qrCodeService.scan(qrCodeContent, onSuccess = {
                if (configurationData.alwaysUseDefaultUser && selectedUser != null) {
                    val role = selectedUser!!.getSelectedGameRole()
                    if (role != null) {
                        onSelectUser(selectedUser!!)
                    } else {
                        "默认用户没有设置默认角色,请选择其他的用户或设置默认角色".errorNotify()
                    }
                } else {
                    showUserDialog = true
                }
            }, onError = {
                it.errorNotify()
            })
        }
    }

    fun onSelectUser(user: User) {
        if (forGoSignWeb) {
            goSignWeb(user)
            forGoSignWeb = false
            return
        }

        if (!qrCodeService.scanSuccess) {
            "你还没有扫描二维码".errorNotify()
            return
        }

        viewModelScope.launchIO {
            qrCodeService.confirm(user, onSuccess = {
                "用户[${user.userInfo.nickname}]扫码登录完成".notify()
            }, onError = {
                it.errorNotify()
            })
            dismissUserDialog()
        }
    }
}