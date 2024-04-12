package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Gray_Dark
import com.lianyi.paimonsnotebook.ui.theme.Font_Primary

/*
* 全圆角标签
* */
@Composable
fun RoundedTag(
    text: String,
    fontSize: TextUnit = 12.sp,
    backGroundColor: Color = CardBackGroundColor_Gray_Dark,
    textColor: Color = Font_Primary,
    paddingValues: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
) {
    Text(
        text = text,
        fontSize = fontSize,
        color = textColor,
        modifier = Modifier
            .clip(CircleShape)
            .background(backGroundColor)
            .padding(paddingValues)
    )
}