package com.lianyi.paimonsnotebook.ui.screen.setting.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.setting.components.SettingOptionGroup
import com.lianyi.paimonsnotebook.ui.screen.setting.viewmodel.SettingScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class SettingsScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[SettingScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        ContentSpacerLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackGroundColor),
            contentPadding = PaddingValues(12.dp, 8.dp),
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SettingOptionGroup(groupName = "界面", list = viewModel.settings)
                    SettingOptionGroup(groupName = "用户", list = viewModel.userSettings)
                    SettingOptionGroup(groupName = "存储", list = viewModel.storageSettings)
                    SettingOptionGroup(groupName = "数据", list = viewModel.dataSettings)
//                    SettingOptionGroup(groupName = "桌面组件", list = viewModel.appwidgetSettings)
                    SettingOptionGroup(groupName = "关于", list = viewModel.about)
                }
            }
        }
    }
}
