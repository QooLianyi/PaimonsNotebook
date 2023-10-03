package com.lianyi.paimonsnotebook.common.components.layout.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor

/*
* 卡片布局
*
* 用于显示整个界面显示卡片的场景
* */
@Composable
fun MaterialCard(
    shadow:Dp = 1.dp,
    radius: Dp = 8.dp,
    backgroundColor:Color = CardBackGroundColor,
    contentPadding:Dp = 16.dp,
    content:@Composable ColumnScope.()->Unit
) {
    Box(modifier = Modifier.shadow(shadow, RoundedCornerShape(radius))) {
        Column(
            modifier = Modifier
                .radius(radius)
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(contentPadding),
            content = content
        )
    }
}