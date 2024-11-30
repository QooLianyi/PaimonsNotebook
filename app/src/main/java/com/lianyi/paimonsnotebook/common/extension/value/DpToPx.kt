package com.lianyi.paimonsnotebook.common.extension.value

import androidx.compose.ui.unit.Dp
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

/*
* compose dp转px
* */
fun Dp.toPx(base: Float = 360f): Float {
    val density = PaimonsNotebookApplication.context.resources.displayMetrics.widthPixels / base
    return this.value * density
}

fun Number.dpToPx(base: Float = 360f) =
    Dp(this.toFloat()).toPx(base)