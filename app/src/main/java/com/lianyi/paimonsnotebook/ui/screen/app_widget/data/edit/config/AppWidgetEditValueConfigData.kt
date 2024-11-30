package com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.config

import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.AppWidgetEditData

data class AppWidgetEditValueConfigData(
    val min: Float,
    val max: Float,
    val onValueChange: (AppWidgetEditData.Component) -> (Float) -> Unit,
    val getProperty: (AppWidgetEditData.Component) -> Float,
    val getShowPropertyPredicate: (AppWidgetEditData.Component, AppWidgetEditData.Component) -> Boolean = { self, other ->
        getProperty.invoke(self) == getProperty.invoke(other)
    }
)
