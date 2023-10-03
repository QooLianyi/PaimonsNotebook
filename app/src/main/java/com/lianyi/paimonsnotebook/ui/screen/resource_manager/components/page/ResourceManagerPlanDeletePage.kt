package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.page

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.loading.LoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingTop
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.card.DiskCacheImageGroupCard
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.DiskCacheGroupData
import com.lianyi.paimonsnotebook.ui.theme.Font_Second

@Composable
fun ResourceManagerPlanDeletePage(
    planDeleteDiskCacheList: List<DiskCacheGroupData.GroupItem>,
    imageSize: Dp,
    onRemoveDeletePlanFromDatabase: (DiskCacheGroupData.GroupItem) -> Unit,
) {
    Crossfade(targetState = planDeleteDiskCacheList.isEmpty(), label = "") {
        if (it) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(3.dp)
                    .paddingTop(100.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                LoadingAnimationPlaceholder()
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "没有计划删除的图片",
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    color = Font_Second
                )
            }
        } else {
            ContentSpacerLazyColumn(modifier = Modifier.fillMaxSize()) {
                item {
                    DiskCacheImageGroupCard(
                        list = planDeleteDiskCacheList,
                        groupName = "以下图片将在下次启动时删除",
                        isMultipleSelect = false,
                        isAllSelect = false,
                        onClickImageGroupActionButton = {},
                        imageSize = imageSize,
                        onClickImage = {
                            onRemoveDeletePlanFromDatabase(it)
                        },
                        onEnabledMultipleSelect = {},
                    )
                }
                item {
                    NavigationBarPaddingSpacer()
                }
            }
        }
    }

}