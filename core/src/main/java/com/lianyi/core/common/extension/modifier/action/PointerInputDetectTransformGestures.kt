package com.lianyi.core.common.extension.modifier.action

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.pointerInputDetectTransformGestures(
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit,
    onGestureEnd: () -> Unit
): Modifier {
    return pointerInput(Unit) {
        detectTransformGesturesAndEnd(
            panZoomLock = false,
            onGesture = { offset, pan, gestureZoom, gestureRotate ->
                onGesture(offset, pan, gestureZoom, gestureRotate)
            },
            onGestureEnd = onGestureEnd
        )
    }
}