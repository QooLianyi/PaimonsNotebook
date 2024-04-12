package com.lianyi.paimonsnotebook.common.components.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.Info

@Composable
fun InfoText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize:TextUnit = 12.sp
) {
    Text(
        text = text,
        color = Info,
        fontSize = fontSize,
        modifier = modifier
    )
}

