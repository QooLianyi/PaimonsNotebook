package com.lianyi.paimonsnotebook.common.database.app_widget_binding.type_converter

import androidx.room.TypeConverter
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.data.AppWidgetConfiguration
import com.lianyi.paimonsnotebook.common.database.util.ITypeConverter
import com.lianyi.paimonsnotebook.common.util.json.JSON

class AppWidgetConfigurationConverter : ITypeConverter<AppWidgetConfiguration, String> {

    @TypeConverter
    override fun convertToEntityProperty(databaseValue: String): AppWidgetConfiguration =
        JSON.parse(databaseValue.ifBlank { "{}" })

    @TypeConverter
    override fun convertToDatabaseValue(entity: AppWidgetConfiguration): String =
        JSON.stringify(entity)
}