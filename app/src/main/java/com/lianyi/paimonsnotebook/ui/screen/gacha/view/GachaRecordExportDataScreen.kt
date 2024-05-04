package com.lianyi.paimonsnotebook.ui.screen.gacha.view

import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.ui.screen.base.file_operation.view.FileOperationScreen
import com.lianyi.paimonsnotebook.ui.screen.base.file_operation.viewmodel.FileOperationScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel.GachaRecordExportDataScreenViewModel

class GachaRecordExportDataScreen : FileOperationScreen() {
    override val viewModel: FileOperationScreenViewModel by lazy {
        ViewModelProvider(this)[GachaRecordExportDataScreenViewModel::class.java]
    }
}