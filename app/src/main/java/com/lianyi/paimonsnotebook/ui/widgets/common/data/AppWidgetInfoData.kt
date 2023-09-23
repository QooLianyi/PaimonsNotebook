package com.lianyi.paimonsnotebook.ui.widgets.common.data

import androidx.compose.runtime.Composable

data class AppWidgetInfoData(
    val label: String,
    val preview: @Composable () -> Unit,
    val defaultWidth:Int,
    val defaultHeight:Int,
)
