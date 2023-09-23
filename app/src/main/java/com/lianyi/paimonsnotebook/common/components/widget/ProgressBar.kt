package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: Float,
    progressColor: Color,
    trackColor: Color = Color(0XFFDCE0F3),
) {
    val pro = if (progress == 0f) 0.001f else progress
    val p = 1 - pro

    Row(
        modifier = modifier
            .background(trackColor, CircleShape)
    ) {
        if (pro > 0.001f){
            Spacer(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(progressColor)
                    .fillMaxHeight()
                    .weight(pro)
            )
        }

        if (p >= 0.001f) {
            Spacer(modifier = Modifier.weight(p))
        }
    }
}