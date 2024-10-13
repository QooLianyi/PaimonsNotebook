package com.lianyi.paimonsnotebook.ui.screen.shortcuts_manager.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.core.ui.components.text.InfoText
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.setting.components.SettingOption
import com.lianyi.paimonsnotebook.ui.screen.setting.components.widgets.SettingsOptionSwitch
import com.lianyi.paimonsnotebook.ui.screen.shortcuts_manager.viewmodel.ShortcutsManagerScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1
import com.lianyi.paimonsnotebook.ui.theme.Error
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.Success

class ShortcutsManagerScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[ShortcutsManagerScreenViewModel::class.java]
    }

    @OptIn(ExperimentalFoundationApi::class)
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
                                title = "启用桌面快捷方式列表",
                                description = "启用时会将选中的功能按照当前顺序添加至快捷方式列表(最多4个)",
                                onClick = viewModel::toggleShortcutsList,
                                slot = {
                                    SettingsOptionSwitch(checked = viewModel.enableShortcutsList)
                                })
                        }

                        item {
                            if (viewModel.selectedShortcutsCount != 0) {
                                InfoText(
                                    text = "已选功能:",
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }

                        itemsIndexed(
                            viewModel.shortcutsList,
                            key = { _, item -> item.modalItemData.name }) { index, item ->

                            Column(modifier = Modifier.animateItemPlacement()) {

                                if (index == viewModel.selectedShortcutsCount) {
                                    InfoText(
                                        text = "可选功能:",
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .padding(vertical = 4.dp)
                                        .radius(8.dp)
                                        .fillMaxWidth()
                                        .background(CardBackGroundColor_Light_1)
                                        .clickable {
                                            viewModel.onShortcutsListItemClick(index, item)
                                        }
                                        .padding(horizontal = 16.dp, vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        painter = painterResource(id = item.modalItemData.icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = Black
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    PrimaryText(
                                        text = item.modalItemData.name,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (item.selected) {
                                        if (index != viewModel.selectedShortcutsCount - 1) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_arrow_down),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .radius(2.dp)
                                                    .size(24.dp)
                                                    .clickable {
                                                        viewModel.changeShortcutsPosition(
                                                            item,
                                                            index
                                                        )
                                                    },
                                                tint = if (viewModel.enableShortcutsList) Error else Black_30
                                            )
                                        }

                                        if (index != 0) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_arrow_up),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .radius(2.dp)
                                                    .size(24.dp)
                                                    .clickable {
                                                        viewModel.changeShortcutsPosition(
                                                            item,
                                                            index,
                                                            true
                                                        )
                                                    },
                                                tint = if (viewModel.enableShortcutsList) Success else Black_30
                                            )
                                        }
                                    }
                                }
                            }
                        }
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

    override fun onDestroy() {
        viewModel.clearList()
        super.onDestroy()
    }

}