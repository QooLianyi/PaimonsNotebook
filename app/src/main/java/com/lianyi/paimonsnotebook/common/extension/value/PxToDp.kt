package com.lianyi.paimonsnotebook.common.extension.value

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

/*
* compose px转dp
* */
fun Number.pxToDp(widthPx: Int = PaimonsNotebookApplication.context.resources.displayMetrics.widthPixels): Dp {
    val screenPixelDensity = widthPx / 360f
    val dpVal = this.toFloat() / screenPixelDensity
    return dpVal.dp
}