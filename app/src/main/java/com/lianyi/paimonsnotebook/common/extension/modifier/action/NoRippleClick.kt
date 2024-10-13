package com.lianyi.paimonsnotebook.common.extension.modifier.action

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun Modifier.noRippleClick(block: () -> Unit): Modifier {
    return this then Modifier.clickable(indication = null, interactionSource = remember {
        MutableInteractionSource()
    }) {
        block()
    }
}