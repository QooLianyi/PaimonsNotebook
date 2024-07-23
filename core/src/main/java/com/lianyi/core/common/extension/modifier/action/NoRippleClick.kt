package com.lianyi.core.common.extension.modifier.action

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

fun Modifier.noRippleClick(block: () -> Unit) = composed {
    Modifier.clickable(indication = null, interactionSource = remember {
        MutableInteractionSource()
    }) {
        block()
    }
}