package com.lianyi.paimonsnotebook.ui.screen.home.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.screen.home.viewmodel.HomeDrawerManagerScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.setting.components.SettingOption
import com.lianyi.paimonsnotebook.ui.screen.setting.components.widgets.SettingsOptionSwitch
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class HomeDrawerManagerScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeDrawerManagerScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme(this) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor)
                ) {

                    ContentSpacerLazyColumn(
                        modifier = Modifier
                            .weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        navigationBarPaddingEnabled = false
                    ) {
                        item {
                            SettingOption(
                                title = "启用自定义主页侧边栏",
                                description = "启用时,",
                                onClick = viewModel::toggleCustomHomeDrawer,
                                slot = {
                                    SettingsOptionSwitch(checked = viewModel.enableCustomHomeDrawer)
                                })
                        }
//
//                        item {
//                            if (viewModel.selectedShortcutsCount != 0) {
//                                InfoText(
//                                    text = "已选功能:",
//                                    modifier = Modifier.padding(vertical = 4.dp)
//                                )
//                            }
//                        }
//
//                        itemsIndexed(
//                            viewModel.shortcutsList,
//                            key = { _, item -> item.modalItemData.name }) { index, item ->
//
//                            Column(modifier = Modifier.animateItemPlacement()) {
//
//                                if (index == viewModel.selectedShortcutsCount) {
//                                    InfoText(
//                                        text = "可选功能:",
//                                        modifier = Modifier.padding(vertical = 4.dp)
//                                    )
//                                }
//
//                                Row(
//                                    modifier = Modifier
//                                        .padding(vertical = 4.dp)
//                                        .radius(8.dp)
//                                        .fillMaxWidth()
//                                        .background(CardBackGroundColor_Light_1)
//                                        .clickable {
//                                            viewModel.onShortcutsListItemClick(index, item)
//                                        }
//                                        .padding(horizontal = 16.dp, vertical = 8.dp),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//
//                                    Icon(
//                                        painter = painterResource(id = item.modalItemData.icon),
//                                        contentDescription = null,
//                                        modifier = Modifier.size(24.dp),
//                                        tint = Black
//                                    )
//
//                                    Spacer(modifier = Modifier.width(8.dp))
//
//                                    PrimaryText(
//                                        text = item.modalItemData.name,
//                                        modifier = Modifier.weight(1f)
//                                    )
//
//                                    if (item.selected) {
//                                        if (index != viewModel.selectedShortcutsCount - 1) {
//                                            Icon(
//                                                painter = painterResource(id = R.drawable.ic_arrow_down),
//                                                contentDescription = null,
//                                                modifier = Modifier
//                                                    .radius(2.dp)
//                                                    .size(24.dp)
//                                                    .clickable {
//                                                        viewModel.changeShortcutsPosition(
//                                                            item,
//                                                            index
//                                                        )
//                                                    },
//                                                tint = if (viewModel.enableShortcutsList) Error else Black_30
//                                            )
//                                        }
//
//                                        if (index != 0) {
//                                            Icon(
//                                                painter = painterResource(id = R.drawable.ic_arrow_up),
//                                                contentDescription = null,
//                                                modifier = Modifier
//                                                    .radius(2.dp)
//                                                    .size(24.dp)
//                                                    .clickable {
//                                                        viewModel.changeShortcutsPosition(
//                                                            item,
//                                                            index,
//                                                            true
//                                                        )
//                                                    },
//                                                tint = if (viewModel.enableShortcutsList) Success else Black_30
//                                            )
//                                        }
//                                    }
//                                }
//                            }
//                        }
                    }

                    TextButton(
                        text = "保存配置并更新",
                        modifier = Modifier.padding(16.dp),
                        onClick = viewModel::submit
                    )

                    NavigationBarPaddingSpacer()
                }
            }
        }
    }
}