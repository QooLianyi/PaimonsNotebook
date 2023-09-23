package com.lianyi.paimonsnotebook.common.components.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.Black


/*
* 自适应尺寸文本
* */
@Composable
fun AutoSizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Black,
    targetTextSize: TextUnit = 16.sp,
    minTextSize:TextUnit = 1.sp,
    textAlign: TextAlign = TextAlign.Start,
    fontWeight: FontWeight? = null,
    maxLines: Int = Int.MAX_VALUE
) {
    val textSize = remember {
        mutableStateOf(targetTextSize)
    }

    Text(
        text = text,
        modifier = modifier,
        fontSize = textSize.value,
        maxLines = maxLines,
        textAlign = textAlign,
        color = color,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis,
        onTextLayout = { textLayoutResult: TextLayoutResult ->
            val lineIndex = textLayoutResult.lineCount - 1
            if (textLayoutResult.isLineEllipsized(lineIndex)) {
                textSize.value = if (textSize.value <= 4.sp) minTextSize else textSize.value * .9f
            }
        }
    )

}