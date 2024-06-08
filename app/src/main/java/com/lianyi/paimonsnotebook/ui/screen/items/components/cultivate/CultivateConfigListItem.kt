package com.lianyi.paimonsnotebook.ui.screen.items.components.cultivate

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.components.widget.TextSliderRtL
import com.lianyi.paimonsnotebook.ui.screen.items.data.cultivate.CultivateConfigData
import com.lianyi.paimonsnotebook.ui.theme.Black

@Composable
fun CultivateConfigListItem(
    config:CultivateConfigData
){
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        NetworkImageForMetadata(
            url = config.iconUrl, tint = if (config.tintIcon) Black else null,
            modifier = Modifier
                .clip(CircleShape)
                .size(24.dp)
        )

        TextSliderRtL(
            value = config.sliderValue,
            onValueChange = config::setLevel,
            range = (1f..config.maxLevel.toFloat()),
            textContentPadding = PaddingValues(0.dp),
            textMinWidth = 90.dp,
            text = {
                "${config.name} Lv.${config.toLevel}"
            },
            fontSize = 12
        )
    }
}