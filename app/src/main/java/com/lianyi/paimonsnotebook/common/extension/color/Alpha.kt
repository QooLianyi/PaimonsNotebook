package com.lianyi.paimonsnotebook.common.extension.color

import androidx.compose.ui.graphics.Color

fun Color.alpha(alpha:Float) = Color(red = this.red, green = this.green, blue = this.blue, alpha = alpha)