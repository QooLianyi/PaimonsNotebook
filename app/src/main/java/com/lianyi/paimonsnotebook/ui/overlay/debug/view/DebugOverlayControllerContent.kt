package com.lianyi.paimonsnotebook.ui.overlay.debug.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.lianyi.paimonsnotebook.R

@Composable
fun DebugOverlayControllerContent() {
    Image(
        painter = painterResource(id = R.drawable.icon_klee_square),
        contentDescription = null,
        modifier = Modifier.fillMaxSize()
    )
}