package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.value.pxToDp
import com.lianyi.paimonsnotebook.ui.theme.Black_5
import com.lianyi.paimonsnotebook.ui.theme.Primary_3
import kotlin.math.abs

@Composable
fun SelectedArea(
    offset: Offset,
    width: Float,
    height: Float
) {
    Box(
        modifier = Modifier
            .graphicsLayer {
                translationX = offset.x
                translationY = offset.y
            }
            .size(
                width = width.pxToDp(density = 720f),
                height = height.pxToDp(density = 720f)
            )
            .background(Black_5)
            .border(1.dp, Primary_3)
    )
}