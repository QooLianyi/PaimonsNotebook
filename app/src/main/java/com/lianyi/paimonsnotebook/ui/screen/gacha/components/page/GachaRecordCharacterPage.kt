package com.lianyi.paimonsnotebook.ui.screen.gacha.components.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.GachaOverviewListItem
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.list_card.ItemListCard
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.White

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CharacterPage(
    items: List<Pair<Int, List<Pair<GachaOverviewListItem, Int>>>>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(items, key = { it.first }) { pair ->
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(text = "${pair.first}星", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    pair.second.forEach { item ->
                        Box(modifier = Modifier.radius(4.dp)) {
                            ItemIconCard(url = item.first.iconUrl, star = item.first.rankType)

                            Text(
                                text = "${item.second}",
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .background(Black_30)
                                    .padding(4.dp, 2.dp)
                                    .align(Alignment.TopEnd),
                                color = White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
            }
        }

    }
}