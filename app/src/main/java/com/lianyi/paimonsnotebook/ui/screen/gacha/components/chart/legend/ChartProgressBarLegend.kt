package com.lianyi.paimonsnotebook.ui.screen.gacha.components.chart.legend

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.components.widget.ProgressBar
import com.lianyi.paimonsnotebook.ui.theme.Black_60

@Composable
fun ChartProgressBarLegend(
    name:String,
    value:String,
    progress:Float,
    progressColor: Color,
    trackColor:Color = Color(0XFFDCE0F3),
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PrimaryText(text = name, fontSize = 14.sp, color = Black_60)
            PrimaryText(text = value, fontSize = 14.sp, color = Black_60)
        }
        Spacer(modifier = Modifier.height(2.dp))
        ProgressBar(
            modifier = Modifier
                .height(10.dp)
                .fillMaxWidth(),
            progress = progress,
            progressColor = progressColor,
            trackColor = trackColor
        )
    }
}