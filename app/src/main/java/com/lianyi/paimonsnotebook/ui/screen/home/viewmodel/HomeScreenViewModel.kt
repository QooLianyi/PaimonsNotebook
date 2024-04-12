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
import com.lianyi.paimonsnotebook.common.database.gacha.data.GachaRecordOverview
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
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.ActivityCalendarData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.NearActivityData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.OfficialRecommendedPostsData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.WebHomeData
import com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement.AnnouncementClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement.AnnouncementContentData
import com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement.AnnouncementData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.common.GachaPoolData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.common.TakumiCommonClient
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

    //祈愿记录总览列表
    val gachaRecordOverviewList = mutableStateListOf<GachaRecordOverview>()

    var showConfirm by mutableStateOf(false)

    private val updateService = UpdateService()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                SettingsHelper.configurationFlow.collect {
                    configurationData = it
                    checkOverlayPermission()
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
//            launch {
//                GachaRecordService.gachaRecordOverviewListFlow.collect {
//                    gachaRecordOverviewList.clear()
//                    gachaRecordOverviewList.addAll(it)
//                }
//            }

            launch {
                dataStoreValuesFirst {
                    val check = it[PreferenceKeys.EnableCheckNewVersion] ?: true
                    if (check) {
                        updateService.checkNewVersion(onFoundNewVersion = {
                            "发现新版本,可前往设置进行更新[关于->派蒙笔记本]".notify()
                        }, onFail = {
                        }, onNotFoundNewVersion = {
                        })
                    }
                }
            }

            launch {
                if (MetadataHelper.metadataNeedUpdate()) {
                    "发现新的元数据,正在更新...".notify()
                    MetadataHelper.updateMetadata(
                        onFailed = {
                            "更新元数据时发生错误,现在使用的仍是旧数据,显示的内容可能会与最新的游戏内容有所差异".errorNotify()
                        },
                        onSuccess = {
                            "元数据更新完毕".notify()
                        }
                    ) {}
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

    private val takumiCommonClient by lazy {
        TakumiCommonClient()
    }

    private val announcementClient by lazy {
        AnnouncementClient()
    }

    //轮播图列表
    val bannerList = mutableStateListOf<WebHomeData.Carousel>()

    //轮播图加载状态
    var bannerStatus by mutableStateOf(LoadingState.Empty)

    val activityCalendarList = mutableStateListOf<ActivityCalendarData.ActivityCalendarDataItem>()
    var activityCalendarStatus by mutableStateOf(LoadingState.Empty)

    //活动列表
    val annList = mutableStateListOf<AnnouncementData.AnnouncementList.AnnouncementItem>()

    //祈愿活动
    val gachaEventList = mutableStateListOf<AnnouncementData.AnnouncementList.AnnouncementItem>()

    //活动
    val eventList = mutableStateListOf<AnnouncementData.AnnouncementList.AnnouncementItem>()

    //活动列表id内容映射
    val annMap = mutableMapOf<Int, AnnouncementContentData.AnnouncementContentItem>()

    lateinit var startActivity: ActivityResultLauncher<Intent>

    val nearActivity = mutableStateListOf<NearActivityData.Hots.Group2.Children.NearActivity>()

    val gachaPools = mutableStateListOf<GachaPoolData.Pool>()

    val noticeList = mutableStateListOf<OfficialRecommendedPostsData.OfficialRecommendedPost>()
    var noticeStatus by mutableStateOf(LoadingState.Empty)

    var isRefreshing by mutableStateOf(false)

    val modalItems = HomeHelper.modalItems

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            getWebHome()
            getOfficialRecommendedPosts()
            getNearActivity()

//            val r2 = announcementClient.getAnnList()
//            if (r2.success) {
//                val map =
//                    mutableMapOf<String, MutableList<AnnouncementData.AnnouncementList.AnnouncementItem>>()
//                r2.data.list.forEach { item ->
//                    val iMap = item.list.groupBy { it.tag_label }
//                    iMap.forEach { (t, u) ->
//                        if (map[t] == null) map[t] = mutableListOf()
//                        map[t]!!.addAll(u)
//                    }
//                }
//
//                val mEventList = map["活动"]
//                if (!mEventList.isNullOrEmpty()) {
//                    mEventList.removeAll { it.type != 1 }
//
//                    eventList.clear()
//                    eventList.addAll(mEventList)
//                }
//
//                val mGachaEventList = map["扭蛋"]
//                if (!mGachaEventList.isNullOrEmpty()) {
//                    gachaEventList.clear()
//                    gachaEventList.addAll(mGachaEventList)
//                    gachaEventList.sortBy { it.ann_id }
//                }
//            } else {
//                "获取活动失败".errorNotify()
//            }
//
//            val result = announcementClient.getAnnContent()
//            if (result.success) {
//                annMap.clear()
//                result.data.list.forEach {
//                    annMap[it.ann_id] = it
//                }
//            } else {
//                "获取活动内容失败".errorNotify()
//            }
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

    //获取祈愿记录卡池
    private suspend fun getGachaPool() {
        val result = takumiCommonClient.getGachaPool()
        if (result.success) {
            gachaPools.clear()
            gachaPools.addAll(result.data.list)
        } else {
            "获取当期卡池失败".errorNotify()
        }
    }


    //文章跳转
    fun goPostDetail(
        postId: String,
        postType: PostType = PostType.Default,
    ) {
        HomeHelper.goActivityByIntent {
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

    fun navigateScreen(modalItemData: ModalItemData) {
        HomeHelper.goActivity(modalItemData.target)
    }

    fun <T : Activity> functionNavigate(cls: Class<T>) {
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