package com.lianyi.paimonsnotebook.common.components.layout

import androidx.compose.runtime.Composable

@Composable
fun ShowIf(
    show:Boolean,
    content: @Composable () -> Unit
) {
    if(show){
        content.invoke()
    }
}