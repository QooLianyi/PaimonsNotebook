package com.lianyi.paimonsnotebook.ui.screen.items.components.item.list_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.text.AutoSizeText
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.items.data.ItemListCardData
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.White


@Composable
fun <T> ItemGridListCard(
    data: T,
    itemListCardData: ItemListCardData,
    dataContent:String,
    onClick: (T) -> Unit
) {

    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .radius(4.dp)
                .size(60.dp, 80.dp)
                .border(1.dp, Black_10, RoundedCornerShape(4.dp))
                .clickable {
                    onClick.invoke(data)
                }
        ) {

            Image(
                painter = painterResource(id = itemListCardData.qualityBgResId),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .size(62.dp, 84.dp)
                    .align(Alignment.Center)
            )

            Column(modifier = Modifier.fillMaxSize()) {
                //此处使用Compose Image会稍微降低列表滚动时的性能损耗(原因暂时未知)
                NetworkImage(url = itemListCardData.iconUrl, modifier = Modifier.size(60.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .background(White)
                        .padding(2.dp, 0.dp),
                    contentAlignment = Alignment.Center
                ) {
                    AutoSizeText(
                        text = dataContent,
                        targetTextSize = 10.sp,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}