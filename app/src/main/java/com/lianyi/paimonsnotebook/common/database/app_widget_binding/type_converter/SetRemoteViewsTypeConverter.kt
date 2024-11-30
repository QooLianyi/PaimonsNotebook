package com.lianyi.paimonsnotebook.common.database.app_widget_binding.type_converter

import androidx.room.TypeConverter
import com.lianyi.paimonsnotebook.common.database.util.ITypeConverter
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.ui.widgets.util.enums.RemoteViewsDataType

class SetRemoteViewsTypeConverter : ITypeConverter<Set<RemoteViewsDataType>, String> {

    @TypeConverter
    override fun convertToEntityProperty(databaseValue: String): Set<RemoteViewsDataType> =
        JSON.parse(databaseValue.ifBlank { "[]" }, getParameterizedType(Set::class.java,
            RemoteViewsDataType::class.java))

    @TypeConverter
    override fun convertToDatabaseValue(entity: Set<RemoteViewsDataType>): String =
        JSON.stringify(entity)
}