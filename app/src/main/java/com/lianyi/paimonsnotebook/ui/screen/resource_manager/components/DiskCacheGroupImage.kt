package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.DiskCacheGroupData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiskCacheGroupImage(
    item: DiskCacheGroupData.GroupItem,
    index: Int,
    imageSize: Dp,
    onClickImage: (DiskCacheGroupData.GroupItem) -> Unit,
    onEnabledMultipleSelect: () -> Unit,
) {
    Image(
        painter = rememberAsyncImagePainter(
            model = item.file
        ),
        contentDescription = item.data.name,
        modifier = Modifier
            .size(imageSize)
            .padding(
                when (index) {
                    0 -> PaddingValues(
                        0.dp,
                        0.dp,
                        .5.dp,
                        0.dp
                    )
                    1, 2 -> PaddingValues(
                        .5.dp,
                        0.dp,
                        .5.dp,
                        0.dp
                    )
                    3 -> PaddingValues(
                        .5.dp,
                        0.dp,
                        0.dp,
                        0.dp
                    )
                    else -> PaddingValues(0.dp)
                }
            )
            .combinedClickable(
                onClick = {
                    onClickImage(item)
                },
                onLongClick = {
                    onEnabledMultipleSelect()
                    onClickImage(item)
                }
            ),
        contentScale = ContentScale.Crop
    )
}