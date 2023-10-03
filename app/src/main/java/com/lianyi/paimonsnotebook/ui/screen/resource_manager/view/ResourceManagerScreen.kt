package com.lianyi.paimonsnotebook.ui.screen.resource_manager.view

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.PropertiesDialog
import com.lianyi.paimonsnotebook.common.components.widget.TabBar
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingEnd
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.page.ResourceManagerHomePage
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.page.ResourceManagerPlanDeletePage
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.popup.PopupDiskCacheDetail
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.viewmodel.ResourceManagerViewModel
import com.lianyi.paimonsnotebook.ui.theme.*

//todo 图片管理界面 等待重构
class ResourceManagerScreen : BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[ResourceManagerViewModel::class.java]
    }

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.onBackPressed {
                    finish()
                }
            }
        })

        setContent {
            PaimonsNotebookTheme(this) {
                BoxWithConstraints {
                    val imageSize = maxWidth / 4

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackGroundColor)
                    ) {

                        Crossfade(
                            targetState = viewModel.isMultipleSelect, label = ""
                        ) {
                            if (it) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_chevron_left),
                                        contentDescription = null,
                                        tint = Black,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(1.dp))
                                            .clickable {
                                                viewModel.onBackPressed {

                                                }
                                            }
                                    )

                                    Spacer(modifier = Modifier.width(10.dp))

                                    Text(
                                        text = if (viewModel.selectCount > 0) "已选择${viewModel.selectCount}项" else "请选择图片",
                                        fontSize = 20.sp,
                                        color = Black,
                                        modifier = Modifier.weight(1f)
                                    )

                                    Crossfade(
                                        targetState = viewModel.selectCount > 0,
                                        modifier = Modifier.paddingEnd(16.dp), label = ""
                                    ) { showDeleteSelect ->
                                        if (showDeleteSelect) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_delete),
                                                contentDescription = null,
                                                tint = Black,
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(RoundedCornerShape(1.dp))
                                                    .clickable {
                                                        viewModel.deleteSelectedImage()
                                                    }
                                            )
                                        }
                                    }

                                    val iconTintColor by animateColorAsState(
                                        targetValue = if (viewModel.selectAll) colorAccent else Black,
                                        label = ""
                                    )

                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_select_all),
                                        contentDescription = null,
                                        tint = iconTintColor,
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(1.dp))
                                            .clickable {
                                                viewModel.selectAllAction()
                                            }
                                    )

                                }
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    TabBar(
                                        tabs = viewModel.tabs,
                                        onSelect = viewModel::changeTab,
                                        modifier = Modifier.weight(1f),
                                        indicatorColor = Transparent
                                    )

                                    Box {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_more_vertical),
                                            contentDescription = null,
                                            tint = Black,
                                            modifier = Modifier
                                                .radius(2.dp)
                                                .size(32.dp)
                                                .clickable {
                                                    viewModel.showResourceDropMenu()
                                                }
                                        )

                                        DropdownMenu(
                                            expanded = viewModel.showResourceDropMenu,
                                            onDismissRequest = { viewModel.disableResourceDropMenu() },
                                            modifier = Modifier.wrapContentWidth()
                                        ) {
                                            viewModel.resourceDropMenuOptions.forEachIndexed { index, s ->
                                                DropdownMenuItem(
                                                    onClick = {
                                                        viewModel.resourceManagerDropMenuAction(
                                                            index
                                                        )
                                                    }
                                                ) {
                                                    Box(contentAlignment = Alignment.CenterStart) {
                                                        Text(text = s, fontSize = 16.sp)
                                                    }
                                                }
                                            }
                                        }

                                    }
                                }
                            }
                        }

                        val pageState = rememberPagerState {
                            viewModel.tabs.size
                        }

                        LaunchedEffect(viewModel.currentTabIndex) {
                            pageState.animateScrollToPage(viewModel.currentTabIndex)
                        }

                        HorizontalPager(state = pageState) {
                            when (it) {
                                0 -> ResourceManagerHomePage(
                                    diskCacheDataList = viewModel.diskCacheDataList,
                                    isMultipleSelect = viewModel.isMultipleSelect,
                                    imageSize = imageSize,
                                    onClickImageGroupActionButton = viewModel::clickImageGroupActionButton,
                                    onClickImage = viewModel::clickImage,
                                    onEnableMultipleSelect = viewModel::enableMultipleSelect
                                )

                                1 -> ResourceManagerPlanDeletePage(
                                    planDeleteDiskCacheList = viewModel.planDeleteDiskCacheList,
                                    imageSize = imageSize,
                                    onRemoveDeletePlanFromDatabase = viewModel::removeDeletePlanFromDatabase
                                )

                            }
                        }

                    }

                    //todo 等待compose实装共享元素,将图片展示拆分到其他Screen
                    PopupDiskCacheDetail(
                        showImage = viewModel.showImage,
                        showImageData = viewModel.showImageData,
                        showImageDetailDropMenu = viewModel.showImageDetailDropMenu,
                        imageDetailDropMenuOptions = viewModel.imageDetailDropMenuOptions,
                        onDisableImage = viewModel::disableShowImage,
                        onShowImageDetailDropMenu = viewModel::showImageDetailDropMenu,
                        onDisableImageDetailDropMenu = viewModel::disableImageDetailDropMenu,
                        imageDetailDropMenuAction = viewModel::imageDetailDropMenuAction,
                    )

                    //图片详细信息
                    if (viewModel.showImageDetailInfo) {

                        PropertiesDialog(
                            title = "图片详情",
                            properties = viewModel.getPropertiesDialogData(),
                            onDismissRequest = {
                                viewModel.disableImageDetailInfo()
                            }, onButtonClick = viewModel::propertiesDialogAction,
                            onCopyPropertiesValue = viewModel::copyPropertiesValue
                        )
                    }
                }
            }
        }
    }
}
