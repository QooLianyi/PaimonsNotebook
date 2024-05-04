package com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.ui.screen.base.file_operation.viewmodel.FileOperationScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.gacha.service.GachaItemsImportService
import java.io.File

class GachaRecordExportDataScreenViewModel : FileOperationScreenViewModel() {

    private val importService by lazy {
        GachaItemsImportService()
    }

    override val filePropertiesOperationDialogTitle = "UIGF Json信息"
    override val confirmDialogTitle = "UIGF Json删除"
    override val pageTitle = "UIGF Json管理"

    override fun getDestFile(name: String): File = FileHelper.getUIGFJsonSaveFile(name)

    override fun getFileList(): Array<File> =
        FileHelper.saveFileGachaItemsPath.listFiles() ?: arrayOf()

    override suspend fun getPropertyListData(
        file: File,
        onSuccess: (List<Pair<String, String>>) -> Unit,
        onFail: () -> Unit
    ) {
        val info = importService.tryGetUIGFJsonInfo(file)

        if (info == null) {
            onFail.invoke()
            return
        }

        info.apply {
            onSuccess.invoke(
                listOf(
                    "文件名称" to file.name,
                    "记录UID" to uid,
                    "记录语言" to lang,
                    "记录来源" to export_app,
                    "导出时间" to TimeHelper.getTime(export_timestamp),
                    "导出程序版本" to export_app_version,
                    "UIGF版本" to uigf_version,
                    "时区" to "$region_time_zone",
                )
            )
        }
    }
}