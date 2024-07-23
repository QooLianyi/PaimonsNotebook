package com.lianyi.core.common.extension.modifier.widget.blur_card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.core.ui.theme.BlurCardBackgroundColor


fun Modifier.blurCard(
    blur: Dp = 4.dp,
    blurBackgroundColor: Color = BlurCardBackgroundColor,
    padding: Dp = 12.dp
) =
    this.clip(RoundedCornerShape(4.dp))
        .background(blurBackgroundColor)
        .padding(padding)
        .blur(blur)
