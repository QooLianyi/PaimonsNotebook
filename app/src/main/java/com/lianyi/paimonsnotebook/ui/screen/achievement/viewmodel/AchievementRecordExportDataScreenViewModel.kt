package com.lianyi.paimonsnotebook.ui.screen.achievement.viewmodel

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.ui.screen.achievement.service.AchievementImportService
import com.lianyi.paimonsnotebook.ui.screen.base.file_operation.viewmodel.FileOperationScreenViewModel
import java.io.File

class AchievementRecordExportDataScreenViewModel : FileOperationScreenViewModel() {

    private val importService by lazy {
        AchievementImportService()
    }

    override val filePropertiesOperationDialogTitle = "UIAF Json信息"
    override val confirmDialogTitle = "UIAF Json删除"
    override val pageTitle = "UIAF Json管理"

    override fun getDestFile(name: String): File = FileHelper.getUIAFJsonSaveFile(name)

    override fun getFileList(): Array<File> =
        FileHelper.saveFileAchievementsPath.listFiles() ?: arrayOf()

    override suspend fun getPropertyListData(
        file: File,
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onFail: () -> Unit
    ) {
        val info = importService.tryGetUIAFJsonInfo(file)

        if (info == null) {
            onFail.invoke()
            return
        }

        info.apply {
            onSuccess.invoke(
                listOf(
                    "文件名称" to file.name,
                    "UIAF版本" to uiaf_version,
                    "记录来源" to export_app,
                    "导出程序版本" to export_app_version,
                    "导出时间" to TimeHelper.getTime(export_timestamp),
                )
            )
        }
    }
}