package com.lianyi.paimonsnotebook.ui.screen.gacha.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.loading.LoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.widget.ProgressBar
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.UserGameRolesDialog
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.dialog.ChooseGameUidDialog
import com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel.GachaRecordOptionScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.setting.components.SettingOptionGroup
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.Primary_2

class GachaRecordOptionScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[GachaRecordOptionScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerRequestPermissionsResult()
        viewModel.startActivity = registerStartActivityForResult()

        viewModel.storagePermission = this::checkStoragePermission

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
                .background(BackGroundColor)
                .padding(12.dp, 8.dp)
        ) {
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SettingOptionGroup(groupName = "祈愿记录", list = viewModel.gachaSettings)
                    SettingOptionGroup(groupName = "记录获取", list = viewModel.importSettings)
                    SettingOptionGroup(groupName = "记录导出", list = viewModel.exportSettings)
                    SettingOptionGroup(groupName = "关于", list = viewModel.aboutSettings)
                }
            }
        }

        if (viewModel.showLoadingDialog) {
            LazyColumnDialog(
                title = viewModel.loadingDialogTitle,
                titleSpacer = 24.dp,
                onDismissRequest = {},
                buttons = arrayOf()
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        LoadingAnimationPlaceholder(shadowDp = 0.dp)

                        Text(text = viewModel.loadingDialogDescription, fontSize = 14.sp)

                        val progressBarValueAnim by animateFloatAsState(
                            targetValue = viewModel.loadingDialogProgressBarValue,
                            label = ""
                        )

                        ProgressBar(
                            progress = progressBarValueAnim,
                            progressColor = Primary_2,
                            modifier = Modifier.height(8.dp)
                        )
                    }
                }
            }
        }

        if (viewModel.showGameRoleDialog) {
            UserGameRolesDialog(
                onButtonClick = viewModel::dismissGameRoleDialog,
                onDismissRequest = viewModel::dismissGameRoleDialog,
                onSelectRole = viewModel::onSelectGameRole
            )
        }

        if(viewModel.showChooseExportUidDialog){
            ChooseGameUidDialog(
                uidList = viewModel.gachaRecordGameUidList,
                onConfirm = viewModel::confirmExportSelectedUidRecord,
                viewModel::dismissChooseExportUidDialog
            )
        }
    }

    override fun onRequestPermissionsResult(result: Boolean) {
        viewModel.showRequestPermissionDialog = !result
        if (!result) {
            "没有获取到所需权限".warnNotify()
        }
    }

    override fun onStartActivityForResult(result: ActivityResult) {
        viewModel.activityResult(result)
    }
}