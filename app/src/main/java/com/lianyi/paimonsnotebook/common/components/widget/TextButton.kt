package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun TextButton(
    text:String,
    modifier: Modifier = Modifier,
    textSize:TextUnit = 16.sp,
    backgroundColor:Color = Primary,
    textColor:Color = White,
    radius:Dp = 8.dp,
    onClick:()->Unit
) {
    Box(modifier = modifier){
        Row(
            modifier = Modifier
                .radius(radius)
                .fillMaxWidth()
                .height(50.dp)
                .background(backgroundColor)
                .clickable {
                    onClick.invoke()
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = text, fontSize = textSize, color = textColor)
        }
    }
}