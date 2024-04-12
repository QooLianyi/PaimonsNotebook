package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_70
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1


@Composable
fun IconTextHintSlotItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    backGroundColor: Color = CardBackGroundColor_Light_1,
    primaryTextColor: Color = Black,
    secondTextColor: Color = Black_70,
    primaryTextSize: TextUnit = 12.sp,
    secondTextSize: TextUnit = 10.sp,
    radius: Dp = 8.dp,
    onClick: () -> Unit,
    slot: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .radius(radius)
                .fillMaxWidth()
                .background(backGroundColor)
                .clickable {
                    onClick.invoke()
                }
                .padding(12.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = primaryTextSize,
                    color = primaryTextColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(1.dp))
                Text(
                    text = description,
                    fontSize = secondTextSize,
                    color = secondTextColor
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            slot()
        }
    }
}