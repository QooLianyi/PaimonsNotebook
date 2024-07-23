package com.lianyi.paimonsnotebook.ui.screen.gacha.components.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.extension.color.alpha
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.drawArcBorder
import com.lianyi.paimonsnotebook.ui.theme.*

@Composable
fun CircleRingChart(
    title: String,
    description: String,
    values: Array<Pair<Int, Color>>,
    totalCount:Int
) {
    val results = values.map {
        (it.first.toFloat() / totalCount ) * 360f to it.second
    }

    val lineWidth = 48f

    var modifier = Modifier
        .size(180.dp)
        .padding(10.dp)

    var startAngel = 0f

    results.forEach { pair ->
        modifier = modifier.drawArcBorder(
            color = pair.second,
            startAngle = startAngel,
            sweepAngle = pair.first,
            lineCap = StrokeCap.Butt,
            lineWidth = lineWidth
        )
        startAngel += pair.first
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .size(110.dp)
                .background(
                    Brush.radialGradient(
                        0f to Transparent,
                        .73f to Transparent,
                        1f to Black.alpha(.05f)
                    ),
                    CircleShape
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            com.lianyi.core.ui.components.text.PrimaryText(text = title, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = description, fontSize = 14.sp, color = Black_30)
        }

    }
}