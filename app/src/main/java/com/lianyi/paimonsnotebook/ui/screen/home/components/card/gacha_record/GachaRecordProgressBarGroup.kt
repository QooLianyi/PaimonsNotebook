package com.lianyi.paimonsnotebook.ui.screen.home.components.card.gacha_record

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.GachaStar4Color
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color

/*
*
* */
@Composable
internal fun GachaRecordProgressBarGroup(
    gachaName:String,
    star4Count:String,
    star4Progress:Float,
    star5Count:String,
    star5Progress:Float,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = gachaName, fontSize = 16.sp)

        Spacer(modifier = Modifier.height(8.dp))

        GachaRecordProgressBar(
            "五星",
            star5Count,
            GachaStar5Color,
            star5Progress
        )

        Spacer(modifier = Modifier.height(8.dp))

        GachaRecordProgressBar(
            "四星",
            star4Count,
            GachaStar4Color,
            star4Progress
        )

    }
}