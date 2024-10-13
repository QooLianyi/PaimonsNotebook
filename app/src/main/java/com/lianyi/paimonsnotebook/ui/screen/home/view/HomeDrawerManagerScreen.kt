package com.lianyi.paimonsnotebook.ui.screen.home.view

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModelProvider
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.components.list.drag_and_drop_list.components.rememberDragAndDropListState
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.action.dragAutoScrollHandler
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.home.viewmodel.HomeDrawerManagerScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.setting.components.SettingOption
import com.lianyi.paimonsnotebook.ui.screen.setting.components.widgets.SettingsOptionSwitch
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1
import com.lianyi.paimonsnotebook.ui.theme.Info
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class HomeDrawerManagerScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[HomeDrawerManagerScreenViewModel::class.java]
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val lazyListState = rememberLazyListState()
            val dragAndDropListState = rememberDragAndDropListState(
                lazyListState = lazyListState,
                onMove = viewModel::onListItemMove
            )

            PaimonsNotebookTheme(this) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor)
                ) {

                    StatusBarPaddingSpacer()

                    Row(modifier = Modifier.padding(12.dp,6.dp)) {
                        SettingOption(
                            title = "启用自定义首页侧边栏",
                            description = "默认关闭,开启后可以调整首页侧边栏中的功能列表顺序,以及控制是否在侧边栏中显示",
                            onClick = viewModel::toggleCustomHomeDrawer,
                            slot = {
                                SettingsOptionSwitch(checked = viewModel.enableCustomHomeDrawer)
                            }
                        )
                    }

                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .dragAutoScrollHandler(
                                listState = lazyListState,
                                onDrag = { change, dragAmount ->
                                    change.consume()
                                    dragAndDropListState.onDrag(dragAmount)
                                },
                                onDragStart = dragAndDropListState::onDragStart,
                                onDragEnd = dragAndDropListState::onDragInterrupted,
                                onDragCancel = dragAndDropListState::onDragInterrupted,
                                autoScrollThreshold = 30f
                            )
                            .weight(1f),
                        contentPadding = PaddingValues(
                            12.dp, 6.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(
                            viewModel.showModalItems,
                            key = { _, e -> e.targetClass }
                        ) { index, item ->
                            val modalItem = viewModel.getModelItemByClassName(item.targetClass)
                                ?: return@itemsIndexed

                            Row(
                                modifier = Modifier
                                    .then(
                                        dragAndDropListState.elementDisplacement
                                            .takeIf {
                                                index == dragAndDropListState.currentIndexOfDraggedItem
                                            }
                                            .let {
                                                if (it != null && viewModel.enableCustomHomeDrawer) {
                                                    Modifier
                                                        .zIndex(1f)
                                                        .graphicsLayer {
                                                            translationY = it
                                                        }
                                                } else Modifier.animateItemPlacement()
                                            }
                                    )
                                    .radius(8.dp)
                                    .fillMaxWidth()
                                    .background(CardBackGroundColor_Light_1)
                                    .clickable {
                                        viewModel.toggleModalItemState(item, index)
                                    }
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    painter = painterResource(id = modalItem.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (item.disable) Info else Black
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                PrimaryText(
                                    text = modalItem.name,
                                    modifier = Modifier.weight(1f),
                                    color = if (item.disable) Info else Black
                                )

                                PrimaryText(
                                    text = if (item.disable) "已禁用" else "第${index + 1}位",
                                    textSize = 12.sp,
                                    color = if (item.disable) Info else Black
                                )
                            }
                        }
                    }

                    TextButton(
                        text = "保存配置并更新",
                        modifier = Modifier.padding(12.dp),
                        onClick = viewModel::submit,
                        textSize = 14.sp
                    )

                    NavigationBarPaddingSpacer()
                }
            }
        }
    }
}