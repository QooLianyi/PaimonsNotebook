package com.lianyi.paimonsnotebook.common.extension.modifier.action

import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

fun Modifier.pointerInputDetectTransformGestures(
    panZoomLock: Boolean = false,
    isTransformInProgressChanged: (Boolean) -> Unit,
    onGesture: (centroid: Offset, pan: Offset, zoom: Float, rotation: Float) -> Unit
) :Modifier {
    return pointerInput(Unit) {
        detectTransformGestures(
            panZoomLock = panZoomLock,
            onGesture = { offset, pan, gestureZoom, gestureRotate ->
                isTransformInProgressChanged(true)
                onGesture(offset, pan, gestureZoom, gestureRotate)
            }
        )
    }.pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {
                awaitFirstDown(requireUnconsumed = false)
                do {
                    val event = awaitPointerEvent()
                    val canceled = event.changes.any { it.isConsumed }
                } while (!canceled && event.changes.any { it.pressed })
                isTransformInProgressChanged(false)
            }
        }
    }
}