package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 带图标的按钮
* 可自定义圆角与图案
*
* */

@Composable
fun IconButton(
    icon: Painter,
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = White,
    color: Color = Black,
    fontSize: TextUnit = 15.sp,
    shape: Shape = CircleShape,
    block: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .shadow(3.dp, shape)
                .clip(shape)
        ) {
            Row(
                modifier = Modifier
                    .clickable {
                        block()
                    }
                    .background(backgroundColor)
                    .padding(12.dp, 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = text,
                        modifier = Modifier.size(24.dp),
                        tint = color
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Text(text = text, color = color, fontSize = fontSize)
            }
        }
    }
}