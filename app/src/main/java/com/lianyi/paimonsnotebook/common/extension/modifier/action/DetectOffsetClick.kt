package com.lianyi.paimonsnotebook.common.extension.modifier.action

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput


/*
* 返回点击位置的点击时间
* */
fun Modifier.detectOffsetClick(onClick: (Offset) -> Unit):Modifier = composed {
    var tapped by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    this.indication(interactionSource, LocalIndication.current)
        .pointerInput(Unit) {
            detectTapGestures(onPress = { offset ->
                tapped = true

                val press = PressInteraction.Press(offset)
                interactionSource.emit(press)

                tryAwaitRelease()

                interactionSource.emit(PressInteraction.Release(press))

                tapped = false
                onClick.invoke(offset)
            })
        }
}