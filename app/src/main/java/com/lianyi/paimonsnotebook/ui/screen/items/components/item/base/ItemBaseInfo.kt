package com.lianyi.paimonsnotebook.ui.screen.items.components.item.base

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.StarGroup
import com.lianyi.paimonsnotebook.ui.theme.White_40

@Composable
internal fun ItemBaseInfo(
    name: String,
    starCount: Int,
    iconUrl: String = ""
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            modifier = Modifier.height(70.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            PrimaryText(
                text = name,
                fontSize = 20.sp
            )

            StarGroup(
                starCount = starCount,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(White_40)
                    .padding(8.dp, 4.dp),
            )
        }

        if (iconUrl.isNotEmpty()) {
            NetworkImageForMetadata(
                iconUrl,
                modifier = Modifier.size(70.dp)
            )
        }
    }
}