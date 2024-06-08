package com.lianyi.paimonsnotebook.ui.screen.items.components.item.material

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard

fun LazyGridScope.materialTitle(
    list: List<Material>,
    onClickMaterial: (Material, IntSize, Offset) -> Unit,
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

            list.forEach { material ->
                var size = IntSize.Zero
                var offset = Offset.Zero
                Box(modifier = Modifier
                    .radius(4.dp)
                    .onGloballyPositioned {
                        size = it.size
                        offset = it.positionInRoot()
                    }
                    .clickable {
                        onClickMaterial.invoke(material,size,offset)
                    }
                ) {
                    ItemIconCard(
                        url = material.iconUrl,
                        size = 34.dp,
                        star = material.RankLevel,
                        borderRadius = 4.dp
                    )
                }
            }
        }
    }
}