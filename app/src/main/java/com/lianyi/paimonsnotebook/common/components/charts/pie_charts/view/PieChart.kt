package com.lianyi.paimonsnotebook.common.components.charts.pie_charts.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import com.lianyi.paimonsnotebook.common.components.charts.pie_charts.data.PieChartData

@Composable
fun PieChart(
    data:List<PieChartData>,
    modifier: Modifier = Modifier,
    radius:Float
) {
    Box(modifier = modifier
        .fillMaxSize()
        .drawBehind {

            val width = size.width
            val height = size.height

            val sum = data.sumOf { it.value }

            val oneAngleValue = 360 / sum

            var currentStartAngle = -90f

            data.forEach { item ->

                val sweepAngle = (item.value * oneAngleValue).toFloat()

                drawArc(
                    color = item.color,
                    startAngle = currentStartAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(
                        radius * 2f,
                        radius * 2f
                    ),
                    topLeft = Offset(
                        (width - radius * 2f) / 2f,
                        (height - radius * 2f) / 2f
                    )
                )
                currentStartAngle += sweepAngle
            }
        }) {

    }

}