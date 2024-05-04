package com.lianyi.paimonsnotebook.ui.screen.achievement.viewmodel

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.text.InfoText
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.achievement.entity.AchievementUser
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.extension.scope.launchMain
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.extension.string.notify
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uiaf.UIAFHelper
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.ui.screen.achievement.service.AchievementExportService
import com.lianyi.paimonsnotebook.ui.screen.achievement.service.AchievementImportService
import com.lianyi.paimonsnotebook.ui.screen.achievement.util.enums.AchievementActionType
import com.lianyi.paimonsnotebook.ui.screen.achievement.view.AchievementRecordExportDataScreen
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.setting.data.OptionListData
import com.lianyi.paimonsnotebook.ui.theme.Primary_2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class AchievementOptionScreenViewModel : ViewModel() {

    private var selectedUser by mutableStateOf<AchievementUser?>(null)

    val userList = mutableListOf<AchievementUser>()

    private val achievementUserDao = PaimonsNotebookDatabase.database.achievementUserDao

    private val achievementsDao = PaimonsNotebookDatabase.database.achievementsDao

    init {
        viewModelScope.launchMain {
            launchMain {
                achievementUserDao.getSelectedUserFlow().collect {
                    selectedUser = it
                }
            }
            launchMain {
                achievementUserDao.getUserListFlow().collect {
                    userList.clear()
                    userList += it
                }
            }
        }
    }

    private lateinit var activityResultFile: File

    //加载对话框
    var showLoadingDialog by mutableStateOf(false)
        private set

    //操作类型
    private var actionType = AchievementActionType.None

    //导入文件属性对话框
    var showImportResultDialog by mutableStateOf(false)

    //导入属性列表
    val importPropertyList = mutableListOf<Pair<String, String>>()

    //显示请求权限对话框
    var showRequestPermissionDialog by mutableStateOf(false)

    //确认删除对话框
    var showConfirmDeleteDialog by mutableStateOf(false)

    lateinit var startActivity: ActivityResultLauncher<Intent>

    //存储权限检查方法
    lateinit var storagePermission: () -> Boolean

    //显示选择档案对话框
    var showSelectAchievementUserDialog by mutableStateOf(false)
        private set

    //显示添加成就管理用户对话框
    var showAddAchievementUserDialog by mutableStateOf(false)
        private set

    private val importService by lazy {
        AchievementImportService(

        )
    }

    private val exportService by lazy {
        AchievementExportService()
    }

    val achievement = listOf(
        OptionListData(
            name = "当前记录",
            description = "显示指定记录的数据",
            onClick = {
                showSelectAchievementUserDialog = true
                actionType = AchievementActionType.SetDefault
            },
            slot = {
                Text(text = selectedUser?.name ?: "", fontSize = 14.sp, color = Primary_2)
            }
        ),
        OptionListData(
            name = "添加成就记录用户",
            description = "添加一个成就记录用户,此用户只用于成就记录功能",
            onClick = {
                showAddAchievementUserDialog = true
            }
        ),
        OptionListData(
            name = "导出的祈愿记录",
            description = "查看与管理导出的祈愿记录",
            onClick = {
                goAchievementRecordExportDataListScreen()
            },
            slot = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_right),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        )
//        OptionListData(
//            name = "删除成就记录用户",
//            description = "选中一个用户,删除该用户的全部记录,此操作不可逆,请谨慎操作",
//            onClick = {
//                showConfirmDeleteDialog = true
//            }
//        )
    )

    val importList = listOf(
        OptionListData(
            name = "UIAF Json导入",
            description = "将现有的UIAF Json数据导入程序",
            onClick = {
                onImportAchievementUIAFJson()
            }
        )
    )

    //当导入成就记录时
    private fun onImportAchievementUIAFJson() {
        if (userList.isEmpty()) {
            "看来你还没有添加成就用户,添加一个成就用户以指定导入的目标".warnNotify(false)
            return
        }

        launchSelectJsonActivity()
    }

    val exportList = listOf(
        OptionListData(
            name = "UIAF Json导出",
            description = "将所选用户的UIAF数据导出",
            onClick = {
                actionType = AchievementActionType.SelectForExport
                showSelectAchievementUserDialog = true
            }
        )
    )

    private fun exportUIAFJson(
        user: AchievementUser
    ) {
        viewModelScope.launchIO {
            val fileName = "${user.name}_${System.currentTimeMillis()}"
            val saveFile = FileHelper.getUIAFJsonSaveFile(fileName)
            exportService.exportAchievementToUIAF(file = saveFile, user)

            "成就记录导出到以下路径:${saveFile.path}".notify(closeable = true)
        }
    }

    val about = listOf(
        OptionListData(
            name = "当前UIAF版本",
            description = "派蒙笔记本当前的UIAF版本",
            onClick = {
                "派蒙笔记本当前的UIAF版本是${UIAFHelper.UIAF_VERSION}".notify()
            },
            slot = {
                InfoText(text = UIAFHelper.UIAF_VERSION)
            }
        ),
        OptionListData(
            name = "关于UIAF",
            description = "点击以查看UIAF的介绍文档以及支持UIAF的相关软件",
            onClick = {
                HomeHelper.goActivityByIntentNewTask {
                    action = Intent.ACTION_VIEW
                    data = Uri.parse(UIAFHelper.UIAF_HOME_PAGE)
                }
            }
        )
    )

    fun onDialogDismissRequest() {
        showSelectAchievementUserDialog = false
    }

    fun createAchievementUser(name: String) {
        if (name.isEmpty() || name.isBlank()) {
            "请输入用户名称".show()
            return
        }

        if (name.length > 10) {
            "名称最长只能为10位".show()
            return
        }

        viewModelScope.launchIO {
            if (achievementUserDao.checkUserExistByName(name)) {
                launchMain {
                    "${name}已存在,请选择其他名称".show()
                }
                return@launchIO
            }

            //如果当前选中用户为空,则将当前用户添加为选中用户
            val selected = if (selectedUser == null) 1 else 0

            val achievementUser = AchievementUser(name = name, selected = selected)
            achievementUserDao.add(achievementUser)

            showAddAchievementUserDialog = false
            "成就记录用户添加成功".notify()
        }
    }

    fun onAddAchievementDialogDismissRequest() {
        this.showAddAchievementUserDialog = false
    }

    fun dismissRequestPermissionDialog() {
        this.showRequestPermissionDialog = false
    }

    private fun launchSelectJsonActivity() {
        showRequestPermissionDialog =
            !(this::storagePermission.isInitialized && storagePermission.invoke())

        if (showRequestPermissionDialog) return

        startActivity.launch(Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/json"
        })
    }

    //当确认删除用户时
    fun onConfirmDeleteUser() {
        showConfirmDeleteDialog = false
        showSelectAchievementUserDialog = true
        actionType = AchievementActionType.SelectForDelete
    }

    //当选中用户时
    fun onSelectUser(user: AchievementUser) {
        when (actionType) {
            AchievementActionType.SelectForImport -> {
                saveToDb(user.id)
            }

            AchievementActionType.SelectForExport -> {
                exportUIAFJson(user)
            }

            AchievementActionType.SelectForDelete -> {
            }

            AchievementActionType.SetDefault -> {
                updateSelectedUser(user)
            }

            else -> {

            }
        }

        showSelectAchievementUserDialog = false
    }

    private fun updateSelectedUser(user: AchievementUser) {
        viewModelScope.launchIO {

            if (selectedUser != null) {
                achievementUserDao.update(selectedUser!!.copy(selected = 0))
            }
            achievementUserDao.update(user.copy(selected = 1))

            "当前显示[${user.name}]的成就记录".notify()
        }
    }

    //保存至数据库
    private fun saveToDb(userId: Int) {
        if (!this::activityResultFile.isInitialized) {
            "未找到UIAF JSON文件".errorNotify()
            return
        }

        viewModelScope.launchIO {
            importService.importAchievementFromUIAFJson(
                userId = userId,
                file = activityResultFile
            )

            //触发room更新
            achievementUserDao.emitSelectedUserFlow()

            "成就记录导入结束".notify()
        }
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

            val resultInfo = importService.tryGetUIAFJsonInfo(activityResultFile)

            resultInfo?.apply {
                importPropertyList.clear()

                importPropertyList +=
                    listOf(
                        "UIAF版本" to uiaf_version,
                        "记录来源" to export_app,
                        "导出程序版本" to export_app_version,
                        "导出时间" to TimeHelper.getTime(export_timestamp),
                    )

                showImportResultDialog = true
            }
        }
    }

    //当点击属性对话框按钮时
    fun onPropertiesDialogButtonClick(index: Int) {
        if (index == 1) {
            showSelectAchievementUserDialog = true
            actionType = AchievementActionType.SelectForImport
        }
        showImportResultDialog = false
    }

    private fun goAchievementRecordExportDataListScreen() {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(AchievementRecordExportDataScreen::class.java)
        }
    }

}