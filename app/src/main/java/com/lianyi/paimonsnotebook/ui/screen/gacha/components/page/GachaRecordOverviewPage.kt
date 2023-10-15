package com.lianyi.paimonsnotebook.ui.screen.gacha.components.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.database.gacha.data.GachaRecordOverview
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.card.GachaRecordCard
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.GachaOverviewListItem

@Composable
fun GachaRecordOverviewPage(
    gachaRecordOverview: GachaRecordOverview,
    overviewListItemMap: Map<String,List<GachaOverviewListItem>>
) {

    ContentSpacerLazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        statusBarPaddingEnabled = false
    ) {
        items(gachaRecordOverview.list, { it.uigfGachaType }) {
            val items by remember(overviewListItemMap) {
                derivedStateOf {
                    overviewListItemMap[it.uigfGachaType] ?: listOf()
                }
            }
            GachaRecordCard(it,items)
        }
    }

}
