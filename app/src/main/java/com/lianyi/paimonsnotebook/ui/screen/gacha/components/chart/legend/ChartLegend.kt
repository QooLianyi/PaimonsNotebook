package com.lianyi.paimonsnotebook.ui.screen.gacha.components.chart.legend

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_60

@Composable
fun ChartLegend(
    legendColor: Color,
    name: String,
    value: String,
    percent: String,
) {

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Spacer(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(14.dp)
                        .background(legendColor)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = name,
                    fontSize = 16.sp,
                    color = Black_60,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = percent,
                fontSize = 16.sp,
                color = Black,
                fontWeight = FontWeight.SemiBold,
            )
        }

        Text(
            text = value,
            fontSize = 16.sp,
            color = Black,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }


}