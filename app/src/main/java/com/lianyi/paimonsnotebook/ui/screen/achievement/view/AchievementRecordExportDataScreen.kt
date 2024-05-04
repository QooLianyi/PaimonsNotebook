package com.lianyi.paimonsnotebook.ui.screen.achievement.view

import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.ui.screen.achievement.viewmodel.AchievementRecordExportDataScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.base.file_operation.view.FileOperationScreen
import com.lianyi.paimonsnotebook.ui.screen.base.file_operation.viewmodel.FileOperationScreenViewModel

class AchievementRecordExportDataScreen : FileOperationScreen() {
    override val viewModel: FileOperationScreenViewModel by lazy {
        ViewModelProvider(this)[AchievementRecordExportDataScreenViewModel::class.java]
    }
}