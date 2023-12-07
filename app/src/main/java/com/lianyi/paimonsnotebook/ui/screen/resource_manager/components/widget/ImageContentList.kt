package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyVerticalGrid
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.value.toPx
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.item.ImageItem
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.item.ImageItemHeader
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.ImageHeaderData
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.extension.gridItemKeyAtPosition
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.extension.imageGridDragHandler
import kotlinx.coroutines.delay
import java.io.File

@Composable
fun ImageContentList(
    diskCacheDataList: List<Pair<ImageHeaderData, List<DiskCache>>>,
    selectedUrls: MutableState<Set<String>>,
    isSelectionMode: MutableState<Boolean>,
    addSelectedUrl: (String) -> Unit,
    selectionAll: (value: Boolean, second: List<DiskCache>) -> Unit,
    getCacheImage: (item: DiskCache) -> File?,
    onClickImage: (selected: Boolean, item: DiskCache) -> Unit,
    onCurrentUserPointerPositionChange: (Offset) -> Unit
) {
    val state = rememberLazyGridState()

    val autoScrollSpeed = remember {
        mutableFloatStateOf(0f)
    }

    var currentUserPointerPosition by remember {
        mutableStateOf(Offset.Zero)
    }

    LaunchedEffect(autoScrollSpeed.floatValue) {
        while (autoScrollSpeed.floatValue != 0f && currentUserPointerPosition != Offset.Zero) {
            state.scrollBy(autoScrollSpeed.floatValue)

            state.gridItemKeyAtPosition(currentUserPointerPosition)
                ?.apply {
                    addSelectedUrl(this)
                }

            delay(10)
        }
    }

    ContentSpacerLazyVerticalGrid(
        columns = GridCells.Adaptive(70.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        statusBarPaddingEnabled = false,
        state = state,
        modifier = Modifier.imageGridDragHandler(
            state,
            selectedUrls,
            autoScrollSpeed,
            60.dp.toPx(),
            currentUserPointerPosition = {
                currentUserPointerPosition = it
                onCurrentUserPointerPositionChange.invoke(it)
            }
        )
    ) {
        diskCacheDataList.forEach { pair ->
            item(span = { GridItemSpan(this.maxLineSpan) }) {
                val selected = remember {
                    derivedStateOf {
                        //当选中的个数小于群组的个数,直接返回false
                        if (selectedUrls.value.size < pair.second.size) return@derivedStateOf false

                        selectedUrls.value.containsAll(pair.second.map { it.url })
                    }
                }
                ImageItemHeader(
                    imageHeaderData = pair.first,
                    selected = selected.value
                ) {
                    selectionAll(selected.value, pair.second)
                }
            }


            items(pair.second, key = { it.url }) { item ->
                val selected =
                    selectedUrls.value.contains(item.url)

                ImageItem(
                    imageCacheFile = getCacheImage(item),
                    selected = selected,
                    isSelectionMode = isSelectionMode.value,
                    modifier = Modifier.clickable {
                        onClickImage(selected, item)
                    }
                )
            }
        }
    }
}