package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.ui.screen.resource_manager.data.ImageHeaderData
import com.lianyi.paimonsnotebook.ui.theme.BlurCardBackgroundColor

@Composable
fun ImageItemHeader(
    imageHeaderData: ImageHeaderData,
    selected: Boolean,
    onClick: (ImageHeaderData) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleText(
            text = "${imageHeaderData.text} | 共${imageHeaderData.count}张",
            fontSize = 18.sp
        )

        Row(modifier = Modifier
            .clip(CircleShape)
            .background(BlurCardBackgroundColor)
            .clickable {
                onClick.invoke(imageHeaderData)
            }
            .padding(8.dp, 4.dp)
        ) {
            TitleText(text = if (selected) "取消全选" else "全选", fontSize = 13.sp)
        }
    }
}