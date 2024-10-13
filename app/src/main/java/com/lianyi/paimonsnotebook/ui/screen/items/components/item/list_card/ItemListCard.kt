package com.lianyi.paimonsnotebook.ui.screen.items.components.item.list_card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.screen.items.data.ItemListCardData
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor

/*
* 适用于列表的物品卡
* */
@Composable
internal fun <T> ItemListCard(
    data: T,
    itemListCardData: ItemListCardData,
    dataContent: String,
    onClick: (T) -> Unit,
    startInformationContentSlot: @Composable () -> Unit = {},
    endInformationContentSlot: @Composable () -> Unit
) {

    Row(modifier = Modifier
        .radius(2.dp)
        .fillMaxWidth()
        .background(CardBackGroundColor)
        .clickable {
            onClick.invoke(data)
        }
        .padding(6.dp, 4.dp)
    ) {

        Box(modifier = Modifier.clip(CircleShape)) {
            ItemIconCard(
                url = itemListCardData.iconUrl,
                star = itemListCardData.quality,
                size = 50.dp,
                borderRadius = 0.dp
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = itemListCardData.name, fontSize = 14.sp)

                startInformationContentSlot.invoke()

                Spacer(modifier = Modifier.weight(1f))

                endInformationContentSlot.invoke()
            }

            if (dataContent.isNotEmpty()) {
                Text(
                    text = dataContent,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
            } else {
                Text(
                    text = itemListCardData.description,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}