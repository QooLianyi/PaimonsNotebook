package com.lianyi.paimonsnotebook.ui.screen.achievement.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.dialog.InputDialog
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.dialog.LoadingDialog
import com.lianyi.paimonsnotebook.common.components.dialog.PropertiesDialog
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.achievement.viewmodel.AchievementOptionScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.setting.components.SettingOptionGroup
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class AchievementOptionScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[AchievementOptionScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerRequestPermissionsResult()
        viewModel.startActivity = registerStartActivityForResult()

        viewModel.checkStoragePermission = this::checkStoragePermission

        setContent {
            PaimonsNotebookTheme(this) {
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
                            SettingOptionGroup(groupName = "成就记录", list = viewModel.achievement)
                            SettingOptionGroup(groupName = "记录获取", list = viewModel.importList)
                            SettingOptionGroup(groupName = "记录导出", list = viewModel.exportList)
                            SettingOptionGroup(groupName = "关于", list = viewModel.about)
                        }
                    }
                }

                if (viewModel.showSelectAchievementUserDialog) {
                    LazyColumnDialog(
                        title = "选择一个成就记录用户",
                        onDismissRequest = viewModel::onDialogDismissRequest,
                        content = {
                            items(viewModel.userList) {
                                Text(text = it.name, modifier = Modifier
                                    .radius(4.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.onSelectUser(it)
                                    }
                                    .padding(8.dp)
                                )
                            }
                        }, onClickButton = {
                            viewModel.onDialogDismissRequest()
                        }
                    )
                }

                if (viewModel.showAddAchievementUserDialog) {
                    InputDialog(
                        title = "添加成就记录用户",
                        placeholder = "请输入用户名称(名称不可重复)",
                        onConfirm = viewModel::createAchievementUser,
                        onCancel = viewModel::onAddAchievementDialogDismissRequest,
                        textMaxLength = 10
                    )
                }

                if (viewModel.showImportResultDialog) {
                    PropertiesDialog(
                        title = "UIAF Json信息",
                        properties = viewModel.importPropertyList,
                        onDismissRequest = { viewModel.showImportResultDialog = false },
                        buttons = arrayOf("取消", "确认导入"),
                        onButtonClick = viewModel::onPropertiesDialogButtonClick
                    )
                }

                if (viewModel.showLoadingDialog) {
                    LoadingDialog()
                }
            }
        }
    }

    override fun onStartActivityForResult(result: ActivityResult) {
        viewModel.activityResult(result)
    }
}