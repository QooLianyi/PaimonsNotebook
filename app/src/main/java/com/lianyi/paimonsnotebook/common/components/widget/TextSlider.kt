package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.value.nonScaledSp
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.Black_40
import com.lianyi.paimonsnotebook.ui.theme.White_60

@Composable
fun TextSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    text: (Float) -> String,
    title: String = "",
    range: ClosedFloatingPointRange<Float> = (0f..1f),
    enabled: Boolean = true,
    textMinWidth: Dp = 70.dp,
    textContentPadding: PaddingValues = PaddingValues(12.dp, 6.dp),
    textContentBackgroundColor: Color = White_60,
    colors: SliderColors = SliderDefaults.colors(
        thumbColor = Black,
        inactiveTrackColor = Black_40,
        activeTrackColor = Black_40,
        disabledThumbColor = Black_40,
        disabledActiveTickColor = Black_10,
        disabledInactiveTrackColor = Black_10,
    ),
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (title.isNotBlank()) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = colors,
            enabled = enabled,
            modifier = Modifier
                .weight(1f)
                .height(30.dp)
        )

        Row(
            modifier = Modifier
                .clip(CircleShape)
                .background(textContentBackgroundColor)
                .requiredWidthIn(textMinWidth,240.dp)
                .padding(textContentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = text(value), fontSize = 16.nonScaledSp(), fontWeight = FontWeight.SemiBold)
        }
    }
}