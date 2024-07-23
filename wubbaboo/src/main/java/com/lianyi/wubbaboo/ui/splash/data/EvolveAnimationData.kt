package com.lianyi.wubbaboo.ui.splash.data

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween

data class EvolveAnimationData(
    val resId: Int,
    val duration: Int,
    val initValue: Float = 0f,
    val targetValue: Float = 360f,
    val repeatMode: RepeatMode = RepeatMode.Restart
) {
    val anim = Animatable(initValue)

    suspend fun play() {
        anim.stop()
        anim.animateTo(
            targetValue,
            infiniteRepeatable(tween(duration, easing = LinearEasing), repeatMode)
        )
    }

    suspend fun stop() {
        anim.stop()
    }
}
