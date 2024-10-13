package com.lianyi.paimonsnotebook.ui.screen.app_widget.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.InformationDialog
import com.lianyi.paimonsnotebook.common.components.layout.ShowIf
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.popup.ColorPickerPopup
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.common.components.widget.TextSlider
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.UserDialog
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.UserGameRolesDialog
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.color.AppWidgetColorConfiguration
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.popup.RemoteViewsPickerPopup
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.preview.GameRoleBindPreview
import com.lianyi.paimonsnotebook.ui.screen.app_widget.components.preview.GameUserBindPreview
import com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel.AppWidgetConfigurationScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.BlurCardBackgroundColor
import com.lianyi.paimonsnotebook.ui.theme.Info
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import kotlin.math.roundToInt

class AppWidgetConfigurationScreen : BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[AppWidgetConfigurationScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(intent)

        viewModel.finishActivity = this::finish

        setContent {
            PaimonsNotebookTheme(this) {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {
        val scope = rememberCoroutineScope()
        ContentSpacerLazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackGroundColor),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    viewModel.configuration.ShowPreview()
                }

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 16.dp)
                        .height(.5.dp)
                        .background(Black_10)
                )

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        com.lianyi.core.ui.components.text.PrimaryText(
                            text = viewModel.configuration.remoteViewsName,
                            textSize = 16.sp
                        )

                        ShowIf(show = viewModel.configuration.showChangeWidget) {
                            Row(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(BlurCardBackgroundColor)
                                    .clickable {
                                        viewModel.showRemoteViewsPickPopup()
                                    }
                                    .padding(4.dp, 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_select_other),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(18.dp),
                                    tint = Black,
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "更换", fontSize = 14.sp, color = Black)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = viewModel.configuration.remoteViewsDesc,
                        fontSize = 12.sp,
                        color = Info
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "*此组件的尺寸为${viewModel.configuration.remoteViewsSize}",
                        fontSize = 12.sp,
                        color = Info
                    )
                }

            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(.5.dp)
                        .background(Black_10)
                )
            }

            item {
                ShowIf(show = viewModel.configuration.showUser) {
                    Column {
                        Text(
                            text = "绑定的用户",
                            fontSize = 14.sp,
                            color = Info
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GameUserBindPreview(
                            user = viewModel.configuration.bindUser,
                            onClick = viewModel::showUserDialog
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            item {
                ShowIf(show = viewModel.configuration.showGameRole) {
                    Column {
                        Text(
                            text = "绑定的角色",
                            fontSize = 14.sp,
                            color = Info
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        GameRoleBindPreview(
                            user = viewModel.configuration.bindUser,
                            role = viewModel.configuration.bindGameRole,
                            onClick = viewModel::showGameRoleSelectDialog
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            item {
                ShowIf(show = viewModel.configuration.showBackgroundColor) {
                    AppWidgetColorConfiguration(
                        title = "背景颜色",
                        colors = viewModel.defaultColorList,
                        customColor = viewModel.configuration.customBackgroundColor,
                        selectedIndex = viewModel.backgroundColorSelectedIndex,
                    ) { color: Color, i: Int ->
                        viewModel.changeBackgroundColor(color, i, scope)
                    }
                }
            }

            item {
                ShowIf(show = viewModel.configuration.showBackgroundRadius) {
                    Column {
                        Text(
                            text = "背景圆角半径",
                            fontSize = 14.sp,
                            color = Info
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        TextSlider(
                            value = viewModel.configuration.backgroundRadius,
                            onValueChange = {
                                viewModel.changeBackgroundRadius(it, scope)
                            },
                            range = (0f..100f),
                            text = {
                                "${it.roundToInt()}"
                            })

                        Text(
                            text = "*背景圆角在一些系统下会被强制设置一个最小值",
                            fontSize = 10.sp,
                            color = Info
                        )
                    }
                }
            }

            item {
                ShowIf(show = viewModel.configuration.showImageTintColor) {
                    AppWidgetColorConfiguration(
                        title = "图片颜色",
                        colors = viewModel.defaultColorList,
                        customColor = viewModel.configuration.customImageTintColor,
                        selectedIndex = viewModel.imageTintColorSelectedIndex,
                    ) { color: Color, i: Int ->
                        viewModel.changeImageTintColor(color, i, scope)
                    }
                }
            }

            item {
                ShowIf(show = viewModel.configuration.showTextColor) {
                    AppWidgetColorConfiguration(
                        title = "字体颜色",
                        colors = viewModel.defaultColorList,
                        customColor = viewModel.configuration.customTextColor,
                        selectedIndex = viewModel.textColorSelectedIndex,
                    ) { color: Color, i: Int ->
                        viewModel.changeTextColor(color, i, scope)
                    }
                }
            }

            item {
                TextButton(
                    if (viewModel.configuration.add) "保存并添加到桌面" else "保存修改并更新",
                    modifier = Modifier.padding(0.dp, 12.dp)
                ) {
                    viewModel.submit()
                }
            }
        }

        ColorPickerPopup(
            visible = viewModel.showColorPickerPopup,
            initialColor = viewModel.getColorPickerPopupInitialColor(),
            onRequestDismiss = viewModel::dismissColorPickerPopup,
            onSelectColor = { color, pointF ->
                viewModel.onColorPickerSelectedColor(color, pointF, scope)
            }
        )

        RemoteViewsPickerPopup(
            remoteViewsClassName = viewModel.configuration.remoteViewsClassName,
            visible = viewModel.showRemoteViewsPickerPopup,
            onRequestDismiss = viewModel::dismissRemoteViewsPickPopup,
            onSelect = viewModel::changeRemoteViews,
            enableMetadata = viewModel.enableMetadata
        )

        if (viewModel.showGameRoleSelectDialog) {
            UserGameRolesDialog(
                onButtonClick = {
                    viewModel.dismissGameRoleSelectDialog()
                },
                onDismissRequest = {
                    viewModel.dismissGameRoleSelectDialog()
                },
                onSelectRole = viewModel::changeGameRole
            )
        }

        if (viewModel.showUserSelectDialog) {
            UserDialog(
                onButtonClick = {
                    viewModel.dismissUserDialog()
                },
                onDismissRequest = viewModel::dismissUserDialog,
                onClickUser = viewModel::changeUser
            )
        }

        if (viewModel.firstEntry) {
            InformationDialog(
                title = "桌面组件配置",
                content = getString(R.string.content_first_entry_appwidget_configuration_screen),
                buttons = viewModel.firstEntryDialogButtons,
                onClickButton = viewModel::onFirstEntryDialogButtonSelect
            )
        }
    }

    override fun onDestroy() {
        viewModel.unregisterAddWidgetSuccessBroadcast()
        super.onDestroy()
    }
}
