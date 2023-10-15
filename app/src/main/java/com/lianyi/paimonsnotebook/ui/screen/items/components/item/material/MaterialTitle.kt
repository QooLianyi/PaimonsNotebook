package com.lianyi.paimonsnotebook.ui.screen.items.components.item.material

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard

fun LazyGridScope.materialTitle(
    list: List<Material>
) {
    item(span = {
        GridItemSpan(this.maxLineSpan)
    }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (list.isEmpty()) "错误的显示内容" else list.first().Name.take(4),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.width(12.dp))

            list.forEach {
                ItemIconCard(
                    url = it.iconUrl,
                    size = 34.dp,
                    star = it.RankLevel,
                    borderRadius = 4.dp
                )
            }
        }
    }
}