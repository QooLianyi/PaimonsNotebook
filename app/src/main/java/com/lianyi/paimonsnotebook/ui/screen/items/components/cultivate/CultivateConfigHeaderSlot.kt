package com.lianyi.paimonsnotebook.ui.screen.items.components.cultivate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.extension.color.alpha
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.StarGroup
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color

@Composable
fun CultivateConfigHeaderSlot(
    iconUrl: String,
    name: String,
    starCount: Int,
    tagSlot: @Composable () -> Unit = {},
    baseInfoSlot: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        NetworkImageForMetadata(
            url = iconUrl,
            modifier = Modifier
                .radius(4.dp)
                .size(60.dp),
            contentScale = ContentScale.FillWidth,
            alignment = BiasAlignment(0f, -0.4f)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )

                StarGroup(
                    starCount = starCount,
                    starSize = 14.dp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(GachaStar5Color.alpha(.1f))
                        .padding(6.dp, 4.dp)
                )
            }

            tagSlot.invoke()
        }

        baseInfoSlot.invoke()
    }
}