package com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.dialog.PropertiesDialog
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.UserAndUid
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.system_service.SystemService
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.event.gacha_info.GachaInfoClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.event.gacha_info.GachaQueryConfigData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.BindingClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.GameAuthKeyData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.GenAuthKeyData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.UserGameRolesDialog
import com.lianyi.paimonsnotebook.ui.screen.gacha.service.GachaItemsExportService
import com.lianyi.paimonsnotebook.ui.screen.gacha.service.GachaItemsImportService
import com.lianyi.paimonsnotebook.ui.screen.gacha.service.GachaLogService
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper
import com.lianyi.paimonsnotebook.ui.screen.setting.data.OptionListData
import com.lianyi.paimonsnotebook.ui.theme.Black_60
import com.lianyi.paimonsnotebook.ui.theme.Gray_F5
import com.lianyi.paimonsnotebook.ui.theme.Primary_2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File


class GachaRecordOptionScreenViewModel : ViewModel() {

    private val gachaRecordGameUid = mutableStateListOf<String>()

    private var currentGameUid by mutableStateOf("")

    lateinit var startActivity: ActivityResultLauncher<Intent>

    private val dao by lazy {
        PaimonsNotebookDatabase.database.gachaItemsDao
    }

    init {
        viewModelScope.launch {
            launch {
                dao.getAllGameUidFlow().collect {
                    gachaRecordGameUid.clear()
                    gachaRecordGameUid += it
                }
            }
            launch {
                dataStoreValues { preferences ->
                    currentGameUid =
                        preferences[PreferenceKeys.GachaRecordCurrentGameUid] ?: ""
                }
            }
        }
    }

    private val bindingClient by lazy {
        BindingClient()
    }

    private val gachaInfoClient by lazy {
        GachaInfoClient()
    }

    private val gachaLogService by lazy {
        GachaLogService()
    }

    var showLoadingDialog by mutableStateOf(false)
    private var loadingDialogCurrentGacheLogIndex = 0
    private var loadingDialogCurrentGacheLogType = UIGFHelper.gachaList.first()

    var showRequestPermissionDialog by mutableStateOf(false)

    var loadingDialogProgressBarValue by mutableFloatStateOf(0f)
    var loadingDialogDescription by mutableStateOf("即将开始")

    var loadingDialogTitle by mutableStateOf("获取祈愿记录")

    private var expandedCurrentGameUidDropMenu by mutableStateOf(false)
    private var showInputUrlDialog by mutableStateOf(false)
    private var inputDialogValue by mutableStateOf("")

    private var showSelectAccountGameRoleDialog by mutableStateOf(false)


    private var showImportUIGFJsonResultDialog by mutableStateOf(false)
    private var importUIGFJsonPropertyList = mutableStateListOf<Pair<String, String>>()

    private var activityResultFile: File? = null

    //存储权限检查方法
    lateinit var storagePermission: () -> Boolean

    //祈愿记录导出服务
    private val exportService by lazy {
        GachaItemsExportService()
    }

    //祈愿记录导入服务
    private val importService by lazy {
        GachaItemsImportService()
    }

    //是否显示游戏角色对话框
    var showGameRoleDialog by mutableStateOf(false)

    val gachaSettings = listOf(
        OptionListData(
            name = "当前账号",
            description = "用于显示指定账号的祈愿记录",
            onClick = {
                if (gachaRecordGameUid.isNotEmpty()) {
                    expandedCurrentGameUidDropMenu = !expandedCurrentGameUidDropMenu
                } else {
                    "当前祈愿记录为空".notify()
                }
            },
            slot = {
                Box(contentAlignment = Alignment.TopEnd) {
                    Text(text = currentGameUid, fontSize = 14.sp, color = Primary_2)

                    DropdownMenu(
                        expanded = expandedCurrentGameUidDropMenu,
                        onDismissRequest = { expandedCurrentGameUidDropMenu = false }
                    ) {
                        gachaRecordGameUid.forEach {
                            Text(
                                text = it,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .clickable {
                                        expandedCurrentGameUidDropMenu = false
                                        viewModelScope.launch {
                                            PreferenceKeys.GachaRecordCurrentGameUid.editValue(
                                                it
                                            )
                                        }
                                    }
                                    .padding(12.dp)
                            )
                        }
                    }
                }
            }
        ),
//        OptionListData(
//            name = "删除记录",
//            description = "从本地删除某个账号的祈愿记录",
//            onClick = {
//
//            }
//        ),
//        OptionListData(
//            name = "全量增加",
//            description = "默认关闭,不再对记录进行重复性验证,将所有能够获取的数据保存到本地",
//            onClick = {
//
//            },
//            slot = {
//
//            }
//        )
    )

    val importSettings = listOf(
        OptionListData(
            name = "通过已登录的账号获取祈愿记录(推荐)",
            description = "使用登录在派蒙笔记本中的账号可以随时随地的获取祈愿记录",
            onClick = {
                showSelectAccountGameRoleDialog = !showSelectAccountGameRoleDialog
            },
            slot = {
                if (showSelectAccountGameRoleDialog) {
                    UserGameRolesDialog(
                        onButtonClick = {
                            showSelectAccountGameRoleDialog = false
                        },
                        onDismissRequest = {
                            showSelectAccountGameRoleDialog = false
                        },
                        onSelectRole = { user, role ->
                            showSelectAccountGameRoleDialog = false
                            generateAuthKeyByAccount(
                                user = user,
                                roleData = role
                            )
                        }
                    )
                }
            }
        ),
        OptionListData(
            name = "通过Url获取祈愿数据",
            description = "通过输入祈愿Url来进行祈愿数据的获取",
            onClick = {
                showInputUrlDialog = !showInputUrlDialog
            },
            slot = {
                if (showInputUrlDialog) {
                    LazyColumnDialog(
                        title = "请输入祈愿记录Url",
                        titleSpacer = 20.dp,
                        onClickButton = {
                            if (it == 0) {
                                showLoadingDialog = true
                                getGaLogFromUrl()
                            }
                            showInputUrlDialog = false
                        },
                        titleTextSize = 16.sp,
                        buttons = arrayOf("取消", "确定"),
                        onDismissRequest = { showInputUrlDialog = false }) {
                        item {
                            InputTextFiled(
                                value = inputDialogValue,
                                onValueChange = this@GachaRecordOptionScreenViewModel::inputDialogValue::set,
                                inputFieldHeight = 200.dp,
                                backgroundColor = Gray_F5,
                                padding = 8.dp,
                                placeholder = {
                                    Text(
                                        text = "请输入祈愿记录Url,并确保各个参数的有效性",
                                        fontSize = 14.sp,
                                        color = Black_60
                                    )
                                }
                            )
                        }
                    }
                }
            }
        ),
        OptionListData(
            name = "从UIGF Json导入",
            description = "从UIGF Json中导入祈愿数据",
            onClick = {
                launchSelectJsonActivity()
            },
            slot = {
                if (showImportUIGFJsonResultDialog) {
                    PropertiesDialog(
                        title = "UIGF Json信息",
                        properties = importUIGFJsonPropertyList,
                        onDismissRequest = { showImportUIGFJsonResultDialog = false },
                        buttons = arrayOf("确认导入", "取消"),
                        onButtonClick = {
                            if (it == 0) saveGachaLogToDB()
                            showImportUIGFJsonResultDialog = false
                        }
                    )
                }
            }
        )
    )

    val exportSettings = listOf(
        OptionListData(
            name = "UIGF Json导出",
            description = "将当前用户的祈愿记录从本地导出为UIGF Json",
            onClick = {
                exportUIGFJson()
            }
        ),
        OptionListData(
            name = "获取祈愿记录URL",
            description = "选择一个角色,将对应角色的祈愿记录URL复制到剪切板",
            onClick = {
                showGameRoleDialog()
            }
        )
    )

    val aboutSettings = listOf(
        OptionListData(
            name = "关于UIGF",
            description = "点击以查看UIGF的介绍文档以及支持UIGF的相关软件",
            onClick = {
            }
        )
    )

    private fun showGameRoleDialog() {
        showGameRoleDialog = true
    }

    fun dismissGameRoleDialog(index: Int = 0) {
        showGameRoleDialog = false
    }

    fun onSelectGameRole(user: User, role: UserGameRoleData.Role) {
        generateAuthKeyByAccount(user, role, true)
    }

    //使用账号生成祈愿密钥
    private fun generateAuthKeyByAccount(
        user: User,
        roleData: UserGameRoleData.Role,
        onlyGetUrl: Boolean = false
    ) {
        showLoadingDialog = true
        loadingDialogTitle = if (onlyGetUrl) "获取祈愿记录URL" else "获取祈愿记录"

        viewModelScope.launch {
            val playerUid = PlayerUid(
                value = roleData.game_uid,
                region = roleData.region
            )
            val result =
                bindingClient.generateAuthenticationKey(UserAndUid(user.userEntity, playerUid))
            if (result.success) {
                val authKey = result.data.asEncodeAuthKeyData()

                if (onlyGetUrl) {
                    SystemService.setClipBoardText(
                        ApiEndpoints.GachaInfoGetGachaLog(
                            GachaQueryConfigData(
                                gachaType = loadingDialogCurrentGacheLogType,
                                gameAuthKeyData = authKey,
                                genAuthKeyData = GenAuthKeyData.createForWebViewGacha(playerUid = playerUid),
                            ).asQueryParameter
                        )
                    )
                    "已将祈愿记录URL复制到剪切板,如果没有复制到剪切板,请检查是否禁用了程序的剪切板权限".notify(
                        autoDismissTime = 6000
                    )
                    dismissGameRoleDialog()
                    showLoadingDialog = false
                } else {
                    getGachaLog(gameAuthKeyData = authKey, playerUid)
                }
            } else {
                "获取失败:${result.retcode},${result.message}".errorNotify()
                showLoadingDialog = false
            }
        }
    }

    //从祈愿Url中获取authKey并尝试获取记录
    private fun getGaLogFromUrl() {
        showLoadingDialog = true
        loadingDialogTitle = "获取祈愿记录"

        val urls = inputDialogValue.split("?")
        if (urls.isEmpty()) {
            "输入URL有误,请检查后再次尝试".errorNotify()
            return
        }

        val params = urls.last().split("&")
        val map = mutableMapOf<String, String>()

        params.forEach {
            val kv = it.split("=")

            if (kv.isEmpty()) {
                "URL参数错误,请检查后再次尝试".errorNotify()
                return
            }

            val k = kv.first().trim()
            val v = kv.last().trim()
            map[k] = v
        }

        val authkey = map["authkey"]
        val authkey_ver = (map["authkey_ver"] ?: "").toIntOrNull()
        val sign_type = (map["sign_type"] ?: "").toIntOrNull()

        val region = map["region"]

        if (authkey.isNullOrBlank() || authkey_ver == null || sign_type == null || region.isNullOrBlank()) {
            "URL中缺少参数,需要的参数:authkey,authkey_ver,sign_type,region".errorNotify()
            showLoadingDialog = false
        } else {
            getGachaLog(
                gameAuthKeyData = GameAuthKeyData(
                    authkey = authkey,
                    authkey_ver = authkey_ver,
                    sign_type = sign_type
                ),
                PlayerUid(value = "", region = region)
            )
        }
    }

    //重置内部变量
    private fun resetLocalValues(gameUid: String = "") {
        loadingDialogCurrentGacheLogIndex = 0
        loadingDialogProgressBarValue = 0f
        loadingDialogCurrentGacheLogType = UIGFHelper.gachaList.first()
        inputDialogValue = ""
        showLoadingDialog = false

        viewModelScope.launch(Dispatchers.IO) {
            PreferenceKeys.GachaRecordCurrentGameUid.editValue("")

            val showUid =
                gameUid.takeIf { currentGameUid.isEmpty() && gameUid.isNotEmpty() } ?: gameUid

            PreferenceKeys.GachaRecordCurrentGameUid.editValue(showUid)
        }

    }

    //通过authKey获取祈愿记录
    private fun getGachaLog(gameAuthKeyData: GameAuthKeyData, playerUid: PlayerUid) {
        var success = true

        viewModelScope.launch(Dispatchers.IO) {

            var endId = "0"

            //当前页面索引
            var pageCount = 1

            //总页面数
            var togglePageCount = 0

            val gameUid = playerUid.value

            //对应卡池类型的最后一条uid,在获取记录时判断是否到达之前的记录处
            var currentGameUidGachaTypeEndId =
                dao.getLastIdByUidAndUIGFGachaType(
                    gameUid,
                    loadingDialogCurrentGacheLogType
                )

            while (true) {
                //更新描述
                loadingDialogDescription =
                    "正在获取${UIGFHelper.getUIGFName(loadingDialogCurrentGacheLogType)}的第${pageCount++}页记录"

                val result = gachaInfoClient.getGachaLogPage(
                    GachaQueryConfigData(
                        gachaType = loadingDialogCurrentGacheLogType,
                        gameAuthKeyData = gameAuthKeyData,
                        genAuthKeyData = GenAuthKeyData.createForWebViewGacha(playerUid = playerUid),
                        endId = endId
                    )
                )

                togglePageCount++

                //请求失败退出
                if (!result.success) {
                    "获取记录时发生了异常:${result.errorMsg}".errorNotify()
                    success = false
                    break
                }

                val list = result.data.list

                //需要添加的记录列表
                val shouldAddItemList = list.takeWhile {
                    it.id != currentGameUidGachaTypeEndId
                }

                //从记录中获取uid
                if (shouldAddItemList.isNotEmpty()) {
                    //添加至数据库,此处的数据长度在[0,20]
                    importService.gachaItemListFlush(shouldAddItemList.map {
                        val id = gachaLogService.getItemIdByName(it.name)
                        it.asGachaItems(itemId = "$id")
                    })
                }

                //重新设置当前记录的最后记录的id
                endId = if (shouldAddItemList.size < 20) {
                    //更换卡池
                    nextGachaLogItemType()

                    //所有卡池都遍历完毕
                    if (loadingDialogCurrentGacheLogType.isBlank()) {
                        break
                    }

                    //重置当前页面索引
                    pageCount = 1


                    //设置当前祈愿卡池最后的ID
                    currentGameUidGachaTypeEndId =
                        dao.getLastIdByUidAndUIGFGachaType(
                            gameUid,
                            loadingDialogCurrentGacheLogType
                        )

                    ""
                } else {
                    //重新设置指定卡池的最后记录的id
                    list.last().id
                }

                //每10页停留5秒,否则随机1~2秒
                val delayTime = if (togglePageCount % 10 == 0) {
                    5000L
                } else {
                    (1000L..2000L).random()
                }

                delay(delayTime)
            }

            if (success) {
                "祈愿记录获取完毕".notify(closeable = true)
            }

            resetLocalValues(gameUid = gameUid)
        }
    }

    private fun nextGachaLogItemType() {
        loadingDialogCurrentGacheLogType =
            if (++loadingDialogCurrentGacheLogIndex < UIGFHelper.uigfGachaTypeCount) {
                UIGFHelper.gachaList[loadingDialogCurrentGacheLogIndex]
            } else {
                ""
            }
        loadingDialogProgressBarValue =
            loadingDialogCurrentGacheLogIndex.toFloat() / UIGFHelper.uigfGachaTypeCount
    }

    private fun launchSelectJsonActivity() {
        showRequestPermissionDialog =
            !(this::storagePermission.isInitialized && storagePermission.invoke())

        if (showRequestPermissionDialog) return

        startActivity.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/json"
        })
    }

    fun activityResult(result: ActivityResult) {
        if (result.resultCode != Activity.RESULT_OK) {
            return
        }

        val data = result.data?.data
        if (data == null) {
            "文件数据为空,请检查后再次尝试:data is null".warnNotify()
            return
        }

        val file = FileHelper.uriToFile(data)

        if (file == null) {
            "文件数据为空,请检查后再次尝试:data to uri is null".warnNotify()
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            activityResultFile = file

            showLoadingDialog = true
            val resultInfo = importService.tryGetUIGFJsonInfo(file)

            showLoadingDialog = false

            resultInfo?.apply {
                importUIGFJsonPropertyList.clear()

                importUIGFJsonPropertyList +=
                    listOf(
                        "记录UID" to uid,
                        "记录语言" to lang,
                        "记录来源" to export_app,
                        "导出时间" to TimeHelper.getTime(export_timestamp),
                        "导出程序版本" to export_app_version,
                        "UIGF版本" to uigf_version
                    )

                showImportUIGFJsonResultDialog = true
            }
        }
    }

    private fun saveGachaLogToDB() {
        if (activityResultFile == null) {
            "还未选择文件".warnNotify()
            return
        }

        showLoadingDialog = true
        loadingDialogDescription = "正在将数据保存至本地数据库"

        viewModelScope.launch(Dispatchers.IO) {

            importService.importGachaRecordFromUIGFJson(activityResultFile!!, gachaLogService)

            showLoadingDialog = false
            "祈愿记录导入结束".notify(closeable = true)

            val gameUid = importService.tryGetUIGFJsonInfo(activityResultFile!!)?.uid ?: ""
            resetLocalValues(gameUid)
        }
    }

    private fun exportUIGFJson() {
        if (currentGameUid == "") {
            "当前没有选中用户".warnNotify(false)
            return
        }

        showRequestPermissionDialog =
            !(this::storagePermission.isInitialized && storagePermission.invoke())

        if (showRequestPermissionDialog) return

        loadingDialogTitle = "导出祈愿记录"
        loadingDialogDescription = "正在导出数据,导出的时长与数据量有关"
        showLoadingDialog = true

        viewModelScope.launch(Dispatchers.IO) {
            val file = FileHelper.getUIGFJsonSaveFile(currentGameUid)
            exportService.exportGachaRecordToUIGFJson(currentGameUid, file)

            showLoadingDialog = false

            "祈愿记录导出到:${file.path}".notify(closeable = true)
        }
    }
}