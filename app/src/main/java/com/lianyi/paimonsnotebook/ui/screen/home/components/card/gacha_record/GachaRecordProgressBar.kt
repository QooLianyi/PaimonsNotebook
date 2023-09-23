package com.lianyi.paimonsnotebook.ui.screen.home.components.card.gacha_record

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.color.alpha
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color
import com.lianyi.paimonsnotebook.ui.theme.Primary_1
import com.lianyi.paimonsnotebook.ui.theme.White

/*
*
* */
@Composable
internal fun GachaRecordProgressBar(
    name: String,
    value: String,
    color: Color,
    progress: Float,
) {

    Box(contentAlignment = Alignment.Center) {
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .radius(4.dp)
                .height(40.dp)
                .fillMaxWidth(),
            color = color.alpha(.25f),
            backgroundColor = White
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = name, color = color, fontSize = 14.sp)
            Text(text = value, color = color, fontSize = 14.sp)
        }

    }
}