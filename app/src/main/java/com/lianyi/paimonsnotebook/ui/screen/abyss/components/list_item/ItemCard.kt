package com.lianyi.paimonsnotebook.ui.screen.abyss.components.list_item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.text.AutoSizeText
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun ItemCard(
    url:String,
    text: String,
    star:Int = 0,
    width: Dp = 55.dp,
    height: Dp = 70.dp
) {
    Column(
        modifier = Modifier
            .radius(4.dp)
            .size(width, height)
    ) {
        ItemIconCard(
            url = url,
            star = star,
            size = width
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height - width)
                .background(White)
                .padding(2.dp, 0.dp),
            contentAlignment = Alignment.Center
        ) {
            AutoSizeText(
                text = text,
                targetTextSize = 10.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}