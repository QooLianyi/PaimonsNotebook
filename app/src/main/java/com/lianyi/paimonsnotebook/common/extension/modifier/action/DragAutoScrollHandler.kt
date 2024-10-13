package com.lianyi.paimonsnotebook.common.extension.modifier.action

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

//长按list时滑动至屏幕y轴顶部或底部时自动滚动列表
@Composable
fun Modifier.dragAutoScrollHandler(
    listState: LazyListState,
    autoScrollThreshold: Float = 150f,
    onDragStart: (Offset) -> Unit = { },
    onDragEnd: () -> Unit = { },
    onDragCancel: () -> Unit = { },
    onDrag: (change: PointerInputChange, dragAmount: Offset) -> Unit
): Modifier {
    val autoScrollSpeed = remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(autoScrollSpeed.floatValue) {
        if (autoScrollSpeed.floatValue != 0f) {
            while (isActive) {
                listState.scrollBy(autoScrollSpeed.floatValue)
                delay(10)
            }
        }
    }

    return this then Modifier.pointerInput(listState) {
        detectDragGesturesAfterLongPress(
            onDrag = { change, dragAmount ->
                onDrag.invoke(change, dragAmount)

                val distFromBottom =
                    listState.layoutInfo.viewportSize.height - change.position.y
                val distFromTop = change.position.y
                autoScrollSpeed.floatValue = when {
                    distFromBottom < autoScrollThreshold -> autoScrollThreshold - distFromBottom
                    distFromTop < autoScrollThreshold -> -(autoScrollThreshold - distFromTop)
                    else -> 0f
                }
            },
            onDragStart = onDragStart,
            onDragEnd = {
                autoScrollSpeed.floatValue = 0f
                onDragEnd.invoke()
            },
            onDragCancel = {
                autoScrollSpeed.floatValue = 0f
                onDragCancel.invoke()
            }
        )
    }
}