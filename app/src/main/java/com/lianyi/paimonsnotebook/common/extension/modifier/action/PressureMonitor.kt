package com.lianyi.paimonsnotebook.common.extension.modifier.action

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.pressureMonitor(
    onBegin: () -> Unit = {},
    onEnd: () -> Unit = {},
) = composed(
    factory = {
        val interactionSource = remember {
            MutableInteractionSource()
        }
        val isPressed by interactionSource.collectIsPressedAsState()

//            设置长按删除标识
        if (isPressed) {
            onBegin()
            DisposableEffect(Unit) {
                onDispose {
                    onEnd()
                }
            }
        }

        Modifier.selectable(selected = false,
            interactionSource = interactionSource,
            indication = null, onClick = { })
    }
)