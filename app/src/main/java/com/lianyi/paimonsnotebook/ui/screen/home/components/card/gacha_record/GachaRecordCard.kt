package com.lianyi.paimonsnotebook.ui.screen.home.components.card.gacha_record

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.database.gacha.data.GachaRecordOverview
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper

@Composable
internal fun GachaRecordCard(
    item: GachaRecordOverview
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PrimaryText(text = "祈愿记录", fontSize = 16.sp)

            Text(text = item.uid, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            item.list.forEach { item ->
                Row(modifier = Modifier.weight(1f)) {
                    GachaRecordProgressBarGroup(
                        UIGFHelper.getUIGFName(item.uigfGachaType),
                        "${item.gachaTimesMap[4] ?: 0}",
                        item.gachaProgressMap[4] ?: 0f,
                        "${item.gachaTimesMap[5] ?: 0}",
                        item.gachaProgressMap[5] ?: 0f,
                    )
                }
            }
            repeat(item.list.size - 3) {
                Row(modifier = Modifier.weight(1f)) {

                }
            }

        }
    }
}