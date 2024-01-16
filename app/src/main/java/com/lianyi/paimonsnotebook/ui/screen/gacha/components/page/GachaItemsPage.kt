package com.lianyi.paimonsnotebook.ui.screen.gacha.components.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun GachaItemsPage(
    items: List<Pair<Int, List<Pair<GachaOverviewListItem, Int>>>>,
    key: String
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(60.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize())
    {
        items.forEach { pair ->
            val showItem = mutableListOf<Pair<GachaOverviewListItem, Int>>().apply {
                pair.second.forEach {
                    if (key == it.first.type) {
                        this@apply += it
                    }
                }
            }

            if (showItem.isEmpty()) return@forEach

            item(span = {GridItemSpan(this.maxLineSpan)}) {
                Text(text = "${pair.first}星", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }

            items(
                showItem,
                key = { it.first.name }
            ) { item ->
                Box(contentAlignment = Alignment.Center) {
                    Box(modifier = Modifier.radius(4.dp)) {
                        ItemIconCard(
                            url = item.first.iconUrl,
                            star = item.first.rankType,
                            size = 60.dp
                        )
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
            item(span = {GridItemSpan(this.maxLineSpan)}) {
                Text(text = "")
            }
        }
    }
}