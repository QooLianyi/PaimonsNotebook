package com.lianyi.paimonsnotebook.common.database.user.type_converter

import androidx.room.TypeConverter
import com.lianyi.paimonsnotebook.common.database.util.ITypeConverter
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.Cookie


class CookieConverter : ITypeConverter<Cookie, String> {

    @TypeConverter
    override fun convertToEntityProperty(databaseValue: String): Cookie =
        Cookie().apply {
            parse(databaseValue)
        }

    @TypeConverter
    override fun convertToDatabaseValue(entity: Cookie): String = entity.toString()
}