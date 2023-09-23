package com.lianyi.paimonsnotebook.common.components.placeholder

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
* 文本占位符
* 用于处理界面无法显示的数据时显示此组件，提示此处数据异常
*
*
* */
@Composable
fun TextPlaceholder(
    text: String = "此处是一个文本占位符",
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .border(2.dp, Color.Red),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {
        Text(text = text, color = Color.Red, textAlign = TextAlign.Center)
    }
}