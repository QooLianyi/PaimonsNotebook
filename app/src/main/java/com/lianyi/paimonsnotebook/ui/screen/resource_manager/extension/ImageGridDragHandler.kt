package com.lianyi.paimonsnotebook.ui.screen.resource_manager.extension

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.imageGridDragHandler(
    lazyGridState: LazyGridState,
    selectedUrls: MutableState<Set<String>>,
    autoScrollSpeed: MutableState<Float>,
    autoScrollThreshold: Float,
    currentUserPointerPosition: (Offset) -> Unit
) = pointerInput(Unit) {
    var initialKey: String? = null
    var currentKey: String? = null

    detectDragGesturesAfterLongPress(
        onDragStart = { offset ->
            lazyGridState.gridItemKeyAtPosition(offset)?.let { key ->
                if (!selectedUrls.value.contains(key)) {
                    initialKey = key
                    currentKey = key
                    selectedUrls.value += key
                }
            }
            currentUserPointerPosition.invoke(offset)
        },
        onDragCancel = {
            initialKey = null
            autoScrollSpeed.value = 0f
            currentUserPointerPosition.invoke(Offset.Zero)
        },
        onDragEnd = {
            initialKey = null
            autoScrollSpeed.value = 0f
            currentUserPointerPosition.invoke(Offset.Zero)
        },
        onDrag = { change, _ ->
            if (initialKey != null) {
                val distFromBottom =
                    lazyGridState.layoutInfo.viewportSize.height - change.position.y
                val distFromTop = change.position.y

                autoScrollSpeed.value = when {
                    distFromBottom < autoScrollThreshold -> autoScrollThreshold - distFromBottom
                    distFromTop < autoScrollThreshold -> -(autoScrollThreshold - distFromTop)
                    else -> 0f
                }

                lazyGridState.gridItemKeyAtPosition(change.position)?.let { key ->
                    if (currentKey != key) {
                        selectedUrls.value += key
                        currentKey = key
                    }
                }
            }
            currentUserPointerPosition.invoke(change.position)
        }
    )
}