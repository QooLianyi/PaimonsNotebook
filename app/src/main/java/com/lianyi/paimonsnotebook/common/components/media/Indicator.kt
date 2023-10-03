package com.lianyi.paimonsnotebook.common.components.media

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 指示器
* */
@Composable
fun Indicator(
    currentPageIndex: Int,
    count: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = White,
    disableColor: Color = Black_30,
    activeWidth: Dp = 24.dp,
) {
    Row(modifier = modifier) {
        repeat(count) { index ->
            Row(
                modifier = modifier,
                horizontalArrangement = Arrangement.Center
            ) {
                val color by animateColorAsState(
                    targetValue = if (index == currentPageIndex) activeColor else disableColor,
                    tween(300), label = ""
                )
                val width by animateDpAsState(
                    targetValue = if (index == currentPageIndex) activeWidth else 8.dp,
                    tween(300), label = ""
                )

                Spacer(
                    modifier = Modifier
                        .padding(3.dp, 0.dp)
                        .size(width, 8.dp)
                        .clip(CircleShape)
                        .background(color)
                )

            }
        }
    }
}