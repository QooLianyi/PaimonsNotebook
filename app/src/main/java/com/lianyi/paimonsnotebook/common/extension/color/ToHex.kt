package com.lianyi.paimonsnotebook.common.extension.color

import androidx.compose.ui.graphics.Color

fun Color.toHex(): String {
    val red = this.red * 255
    val green = this.green * 255
    val blue = this.blue * 255
    return String.format("#%02x%02x%02x", red.toInt(), green.toInt(), blue.toInt())
}