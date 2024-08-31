package com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel

import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
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
        try {
            onSuccess.invoke(importService.getUIGFJsonPropertyListCompat(file))
        } catch (e: Exception) {
            "${e.message}".errorNotify()
        }
    }
}