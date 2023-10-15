package com.lianyi.paimonsnotebook.ui.screen.resource_manager.extension

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect

fun LazyGridState.gridItemKeyAtPosition(hitPoint: Offset): String? =
    layoutInfo.visibleItemsInfo.find { itemInfo ->
        itemInfo.size.toIntRect().contains(hitPoint.round() - itemInfo.offset)
    }?.key as? String