package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.Primary_2
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun Switch(
    checked: Boolean,
    checkedThumbColor: Color,
    checkedTrackColor: Color,
    uncheckedThumbColor: Color,
    uncheckedTrackColor: Color,
    borderColor: Color = Black_10,
    onValueChange: (Boolean) -> Unit,
) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) checkedTrackColor else uncheckedTrackColor,
        label = ""
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) checkedThumbColor else uncheckedThumbColor,
        label = ""
    )
    val thumbScale by animateFloatAsState(targetValue = if (checked) 1f else .8f, label = "")

    val borderDp by animateDpAsState(targetValue = if (checked) 0.dp else 1.dp, label = "")
    val offset by animateDpAsState(targetValue = if (checked) 21.dp else 0.dp, label = "")

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(trackColor)
            .border(borderDp, borderColor, CircleShape)
            .clickable {
                onValueChange.invoke(!checked)
            }
            .width(45.dp)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = offset)
                .scale(thumbScale)
        ) {
            Spacer(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(20.dp)
                    .background(thumbColor)
            )
        }
    }
}

@Composable
fun Switch(
    checked: Boolean,
    checkedThumbColor: Color,
    checkedTrackColor: Color,
    uncheckedThumbColor: Color,
    uncheckedTrackColor: Color,
    borderColor: Color = Black_10,
    barWidth: Dp = 45.dp,
    barHeight: Dp = 20.dp
) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) checkedTrackColor else uncheckedTrackColor,
        label = ""
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) checkedThumbColor else uncheckedThumbColor,
        label = ""
    )
    val thumbScale by animateFloatAsState(targetValue = if (checked) 1f else .8f, label = "")

    val borderDp by animateDpAsState(targetValue = if (checked) 0.dp else 1.dp, label = "")
    val offset by animateDpAsState(
        targetValue = if (checked) barWidth - barHeight - 4.dp else 0.dp,
        label = ""
    )

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(trackColor)
            .border(borderDp, borderColor, CircleShape)
            .width(barWidth)
            .padding(2.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = offset)
                .scale(thumbScale)
        ) {
            Spacer(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(barHeight)
                    .background(thumbColor)
            )
        }
    }
}


@Composable
fun Switch(
    checked: Boolean,
) {
    Switch(
        checked = checked,
        checkedThumbColor = White,
        checkedTrackColor = Primary_2,
        uncheckedThumbColor = Primary,
        uncheckedTrackColor = White,
        barWidth = 38.dp,
        barHeight = 16.dp
    )
}



