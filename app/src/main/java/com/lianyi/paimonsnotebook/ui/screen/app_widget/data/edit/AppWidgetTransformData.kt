package com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit

data class AppWidgetTransformData(
    val maxX: Float,
    val minX: Float,
    val maxY: Float,
    val minY: Float,
    val maxWidth: Float,
    val maxHeight: Float,
    val sumWidth: Float,
    val sumHeight: Float
) {
    val sumX = maxX + minX
    val sumY = maxY + minY
}
