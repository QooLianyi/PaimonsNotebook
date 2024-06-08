package com.lianyi.paimonsnotebook.ui.screen.abyss.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.layout.column.TabBarColumnLayout
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.abyss.components.page.AbyssRecordPage
import com.lianyi.paimonsnotebook.ui.screen.abyss.viewmodel.AbyssScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.UserGameRolesDialog
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class AbyssScreen : BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[AbyssScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {
                TabBarColumnLayout(
                    tabs = viewModel.tabs,
                    onTabBarSelect = viewModel::onPageIndexChange,
                    topSlot = {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {

                            Row(
                                modifier = Modifier
                                    .radius(2.dp)
                                    .clickable {
                                        viewModel.showUserGameRoleDialog()
                                    },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = viewModel.currentGameRole?.game_uid ?: "",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                )

                                Spacer(modifier = Modifier.width(4.dp))

                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chevron_down),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    },
                    tabBarPaddingHorizontal = 12.dp
                ) {
                    Crossfade(targetState = viewModel.currentPageIndex, label = "") {
                        when (it) {
                            0 -> {
                                AbyssRecordPage(
                                    abyssData = viewModel.currentAbyssRecord,
                                    loadingState = viewModel.currentAbyssRecordLoadingState,
                                    getAvatarFromMetadata = viewModel::getAvatarFromMetadata,
                                    getMonsterFromMetadata = viewModel::getMonsterFromMetadata
                                )
                            }

                            1 -> {
                                AbyssRecordPage(
                                    abyssData = viewModel.previousAbyssRecord,
                                    loadingState = viewModel.previousAbyssRecordLoadingState,
                                    getAvatarFromMetadata = viewModel::getAvatarFromMetadata,
                                    getMonsterFromMetadata = viewModel::getMonsterFromMetadata
                                )
                            }
                        }
                    }
                }
            }

            if (viewModel.showUserGameRoleDialog) {
                UserGameRolesDialog(
                    onButtonClick = {
                        viewModel.dismissUserGameRoleDialog()
                    },
                    onDismissRequest = viewModel::dismissUserGameRoleDialog,
                    onSelectRole = viewModel::onChangeGameRole
                )
            }

            if (viewModel.showConfirmDialog) {
                ConfirmDialog(
                    content = "进行验证才能继续进行查询,点击确认前往验证界面",
                    onConfirm = viewModel::goValidateScreen,
                    onCancel = viewModel::dismissConfirmDialog
                )
            }
        }
    }
}