package com.lianyi.core.ui.components.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DividerText(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color(0x4D000000),
    textSpacer: Dp = 10.dp,
    dividerWidth: Dp = 20.dp,
    dividerHeight: Dp = 1.dp,
    dividerColor: Color = Color(0x33000000),
    fontSize: TextUnit = 13.sp,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .height(dividerHeight)
                .width(dividerWidth)
                .background(dividerColor)
        )

        Text(
            text = text,
            color = textColor,
            fontSize = fontSize,
            modifier = Modifier.padding(textSpacer, 0.dp)
        )

        Spacer(
            modifier = Modifier
                .height(dividerHeight)
                .width(dividerWidth)
                .background(dividerColor)
        )
    }
}