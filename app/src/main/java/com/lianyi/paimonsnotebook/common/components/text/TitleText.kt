package com.lianyi.paimonsnotebook.common.components.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 15.sp,
    bold: Boolean = true,
    color: Color = Color(0xff333333),
    textAlign: TextAlign = TextAlign.Start
) {
    Text(text = text,
        fontSize = fontSize,
        modifier = modifier,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        color = color,
        textAlign = textAlign
    )
}