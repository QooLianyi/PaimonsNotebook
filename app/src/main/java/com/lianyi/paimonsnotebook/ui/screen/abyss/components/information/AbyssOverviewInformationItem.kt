package com.lianyi.paimonsnotebook.ui.screen.abyss.components.information

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black

@Composable
internal fun AbyssOverviewInformationItem(
    name: String,
    value: String,
    url: String = ""
) {
    Row(
        modifier = Modifier
            .radius(4.dp)
            .fillMaxWidth()
//            .background(White_50)
//            .padding(4.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = name, fontSize = 12.sp, color = Black)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = value,
                fontSize = 12.sp,
                color = Black
            )

            if (url.isNotBlank()) {
                Spacer(modifier = Modifier.width(6.dp))

                NetworkImageForMetadata(url = url, modifier = Modifier
                    .clip(CircleShape)
                    .size(24.dp))
            }
        }
    }
}