package com.lianyi.core.ui.components.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.theme.Font_Primary

@Composable
fun PrimaryText(
    modifier: Modifier = Modifier,
    text: String,
    textSize: TextUnit = 15.sp,
    bold: Boolean = true,
    maxLines: Int = 1,
    color: Color = Font_Primary,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        fontSize = textSize,
        modifier = modifier,
        fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        color = color,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}