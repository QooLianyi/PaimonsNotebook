package com.lianyi.paimonsnotebook.common.extension.value

import androidx.compose.ui.unit.TextUnit
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

/*
* compose sp转px
* */
fun TextUnit.toPx():Float{
    val density = PaimonsNotebookApplication.context.resources.displayMetrics.widthPixels / 360f
    return this.value * density
}