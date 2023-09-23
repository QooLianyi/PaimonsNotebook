package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.page

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationPaddingSpacer
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.card.DiskCacheImageGroupCard
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.DiskCacheGroupData

@Composable
fun ResourceManagerHomePage(
    diskCacheDataList: List<DiskCacheGroupData>,
    isMultipleSelect: Boolean,
    imageSize: Dp,
    onClickImageGroupActionButton: (DiskCacheGroupData) -> Unit,
    onClickImage: (DiskCacheGroupData.GroupItem, DiskCacheGroupData) -> Unit,
    onEnableMultipleSelect: () -> Unit,
) {

    LazyColumn(modifier = Modifier
        .fillMaxSize()) {

        items(diskCacheDataList) { group ->

            Column {

                DiskCacheImageGroupCard(
                    list = group.list,
                    groupName = "${group.date} | 共${group.list.size}张",
                    isMultipleSelect = isMultipleSelect,
                    isAllSelect = group.isAllSelect,
                    onClickImageGroupActionButton = {
                        onClickImageGroupActionButton(group)
                    },
                    imageSize = imageSize,
                    onClickImage = {
                        onClickImage(it, group)
                    },
                    onEnabledMultipleSelect = onEnableMultipleSelect,
                )

            }

        }

        item {
            NavigationPaddingSpacer()
        }

    }

}