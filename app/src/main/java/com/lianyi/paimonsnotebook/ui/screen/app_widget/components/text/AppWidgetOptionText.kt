package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.text

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Font_Normal
import com.lianyi.paimonsnotebook.ui.theme.Transparent
import com.lianyi.paimonsnotebook.ui.theme.White_60

@Composable
internal fun AppWidgetOptionText(
    text: String,
    selected: Boolean,
    backgroundColor: Color = White_60,
    onClick: () -> Unit,
) {
    val borderColor by animateColorAsState(targetValue = if (selected) Black else Transparent)
    Text(
        text = text,
        fontSize = 12.sp,
        color = Font_Normal,
        modifier = Modifier
            .radius(4.dp)
            .border(3.dp, borderColor, RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .clickable {
                onClick.invoke()
            }
            .padding(6.dp, 4.dp)
    )
}