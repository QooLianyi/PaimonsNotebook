package com.lianyi.paimonsnotebook.common.components.popup

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.Transparent

@Composable
fun BasePopup(
    visible: Boolean,
    onRequestDismiss: () -> Unit,
    enter: EnterTransition = slideIn(spring()) { IntOffset(-it.width, 0) },
    exit: ExitTransition = slideOut(spring()) { IntOffset(-it.width, 0) },
    backgroundColor:Color = Transparent,
    content: @Composable () -> Unit
) {
    val bgColor = animateColorAsState(targetValue = if (visible) Black_30 else Transparent,
        label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor.value)
            .zIndex(10f)
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = enter,
            exit = exit,
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            content.invoke()
        }
    }
}