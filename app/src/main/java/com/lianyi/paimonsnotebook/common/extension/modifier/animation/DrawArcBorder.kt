package com.lianyi.paimonsnotebook.common.extension.modifier.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke

fun Modifier.drawArcBorder(
    color: Color = Color.Black,
    startAngle: Float = 0f,
    sweepAngle: Float = 360f,
    lineWidth: Float = 10f,
    lineCap: StrokeCap = StrokeCap.Round,
    enabledTrack: Boolean = false,
    trackColor: Color = Color.Gray,
    trackSweepAngle:Float = 360f
) = composed(
    factory = {

        val anim by animateFloatAsState(targetValue = sweepAngle, tween(200))

        Modifier.drawBehind {

            if (enabledTrack) {
                drawArc(trackColor,
                    startAngle = (startAngle - 90f),
                    sweepAngle = trackSweepAngle,
                    false,
                    style = Stroke(lineWidth, cap = lineCap))
            }

            drawArc(color,
                startAngle = (startAngle - 90f),
                sweepAngle = anim,
                false,
                style = Stroke(lineWidth, cap = lineCap))
        }
    },
    inspectorInfo = {

    }
)



