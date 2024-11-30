package com.lianyi.paimonsnotebook.common.extension.modifier.offset

import androidx.compose.ui.geometry.Offset
import kotlin.math.cos
import kotlin.math.sin

fun Offset.rotateBy(angle: Float): Offset {
    val angleInRadians = (Math.PI / 180f).toFloat() * angle
    val newX = x * cos(angleInRadians) - y * sin(angleInRadians)
    val newY = x * sin(angleInRadians) + y * cos(angleInRadians)

    return Offset(newX, newY)
}

fun Offset.biggerThan(other: Offset): Boolean = this.x > other.x && this.y > other.y

fun Offset.getBigger(other: Offset): Offset =
    if (this.biggerThan(other)) {
        this
    } else {
        other
    }

fun Offset.getLess(other: Offset): Offset =
    if (this.biggerThan(other)) {
        other
    } else {
        this
    }
