package com.lianyi.paimonsnotebook.common.extension.value

import androidx.compose.ui.unit.Dp
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

/*
* compose dp转px
* */
fun Dp.toPx():Float{
    val density = PaimonsNotebookApplication.context.resources.displayMetrics.widthPixels / 360f
    return this.value * density
}