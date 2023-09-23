package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.color

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingTop
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Transparent

@Composable
internal fun AppWidgetOptionColor(
    color: Color,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(targetValue = if (selected) Black else Transparent)

    Box(modifier = Modifier
        .paddingTop(6.dp)
        .radius(4.dp)
        .size(40.dp)
        .background(color)
        .border(3.dp, borderColor, RoundedCornerShape(4.dp))
        .clickable {
            onClick.invoke()
            if (selected) {
                println("点击了选中的颜色")
            }
        }
    )
}