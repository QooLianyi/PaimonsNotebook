package com.lianyi.core.common.extension.modifier.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.debugInspectorInfo

/*
* 给予一个布尔值，对对象进行无限的左右旋转与xy缩放
* */

fun Modifier.shake(
    enabled: Boolean = true,
    scale: Float = .85f,
    angle: Float = 10f,
) =
    composed(
        factory = {

            val infiniteTransition = rememberInfiniteTransition(label = "")

            val scaleAnim by animateFloatAsState(
                targetValue = if (enabled) scale else 1f,
                animationSpec = if (enabled) infiniteRepeatable(
                    animation = tween(durationMillis = 200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
                else tween(200), label = ""
            )

            val rotateAnim by if (enabled) {
                infiniteTransition.animateFloat(
                    initialValue = -angle,
                    targetValue = angle,
                    animationSpec = infiniteRepeatable(
                        animation = tween(200, easing = LinearEasing),
                        repeatMode = RepeatMode.Reverse
                    ), label = ""
                )
            } else {
                animateFloatAsState(targetValue = 0f, tween(200), label = "")
            }

            Modifier.graphicsLayer {
                scaleX = scaleAnim
                scaleY = scaleAnim
                rotationZ = rotateAnim
            }
        },
        inspectorInfo = debugInspectorInfo {
            name = "shark"
            properties["enabled"] = enabled
        }
    )
