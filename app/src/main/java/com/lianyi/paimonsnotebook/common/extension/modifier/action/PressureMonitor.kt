package com.lianyi.paimonsnotebook.common.extension.modifier.action

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.pressureMonitor(
    onBegin: () -> Unit = {},
    onEnd: () -> Unit = {},
): Modifier {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isPressed by interactionSource.collectIsPressedAsState()

//设置长按删除标识
    if (isPressed) {
        onBegin.invoke()
        DisposableEffect(Unit) {
            onDispose {
                onEnd.invoke()
            }
        }
    }

    return this then Modifier.selectable(selected = false,
        interactionSource = interactionSource,
        indication = null, onClick = { })
}