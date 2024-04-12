package com.lianyi.paimonsnotebook.ui.screen.resource_manager.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.layout.column.TabBarColumnLayout
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPlaceholder
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.widget.ImageContentList
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.viewmodel.ResourceManagerViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class ResourceManagerScreen : BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[ResourceManagerViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme(this) {
                TabBarColumnLayout(tabs = viewModel.tabs,
                    onTabBarSelect = viewModel::onChangePage,
                    topSlot = {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Crossfade(targetState = viewModel.isSelectionMode.value, label = "") {
                                if (it) {
                                    Icon(painter = painterResource(id = R.drawable.ic_delete),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .radius(4.dp)
                                            .size(28.dp)
                                            .clickable {
                                                viewModel.deleteSelectedImage()
                                            }
                                            .padding(2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }) {
                    Crossfade(targetState = viewModel.currentTabIndex, label = "") { page ->
                        when (page) {
                            1 -> {
                                Crossfade(
                                    targetState = viewModel.planDeleteListIsEmpty,
                                    label = ""
                                ) {
                                    if (it) {
                                        EmptyPlaceholder("当前没有计划删除的图片")
                                    } else {
                                        ImageContentList(
                                            diskCacheDataList = viewModel.diskCachePlanDeleteDataList,
                                            selectedUrls = viewModel.selectedUrls,
                                            isSelectionMode = viewModel.isSelectionMode,
                                            addSelectedUrl = viewModel::addSelectedUrl,
                                            selectionAll = viewModel::selectionAll,
                                            getCacheImage = viewModel::getCacheImage,
                                            onClickImage = viewModel::onClickImage,
                                            onCurrentUserPointerPositionChange = viewModel::changeCurrentUserPointerPosition
                                        )
                                    }
                                }

                            }

                            else -> {
                                ImageContentList(
                                    diskCacheDataList = viewModel.diskCacheDataList,
                                    selectedUrls = viewModel.selectedUrls,
                                    isSelectionMode = viewModel.isSelectionMode,
                                    addSelectedUrl = viewModel::addSelectedUrl,
                                    selectionAll = viewModel::selectionAll,
                                    getCacheImage = viewModel::getCacheImage,
                                    onClickImage = viewModel::onClickImage,
                                    onCurrentUserPointerPositionChange = viewModel::changeCurrentUserPointerPosition
                                )
                            }
                        }
                    }
                }


                if (viewModel.showConfirm) {
                    ConfirmDialog(
                        content = if (viewModel.currentTabIndex == 0) "确定将所选图片移入计划删除队列吗?(共${viewModel.selectedUrls.value.size}张)" else "确定要将选中的图片从计划删除队列中移出吗?(共${viewModel.selectedUrls.value.size}张)",
                        onConfirm = viewModel::confirmDeleteSelectedImage,
                        onCancel = viewModel::dismissConfirm
                    )
                }
            }
        }
    }
}
