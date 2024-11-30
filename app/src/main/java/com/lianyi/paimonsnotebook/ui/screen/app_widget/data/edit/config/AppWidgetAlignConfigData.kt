package com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.config

import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.AppWidgetEditData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.AppWidgetTransformData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.util.enums.ComponentAlignType

data class AppWidgetAlignConfigData(
    val type: ComponentAlignType,
    val onAlign: (AppWidgetEditData.Base.ComponentImpl, AppWidgetTransformData) -> Unit = { _, _ -> },
    val onAlignByValue: (AppWidgetEditData.Base.ComponentImpl, Float) -> Unit = { _, _ -> },
    val onAlignBoth: (AppWidgetEditData.Base.ComponentImpl, Float, Float) -> Float = { _, _, _ -> 0f }
) {

}
