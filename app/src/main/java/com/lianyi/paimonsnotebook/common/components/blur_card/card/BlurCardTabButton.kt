package com.lianyi.paimonsnotebook.common.components.blur_card.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Transparent
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun BlurCardTabButton(
    text: String,
    index: Int,
    currentIndex: Int,
    onClick: (Int) -> Unit,
) {

    val isChecked = index == currentIndex

    val backgroundColorAnim by animateColorAsState(
        targetValue = if (isChecked) Black else Transparent,
        animationSpec = tween(300)
    )
    val textColorAnim by animateColorAsState(
        targetValue = if (isChecked) White else Black,
        animationSpec = tween(300)
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColorAnim)
            .clickable {
                onClick(index)
            }
            .padding(12.dp, 6.dp)
    ) {
        Text(text = text, color = textColorAnim, fontSize = 14.sp)
    }
}