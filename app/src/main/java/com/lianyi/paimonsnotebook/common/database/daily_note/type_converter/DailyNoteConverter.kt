package com.lianyi.paimonsnotebook.common.database.daily_note.type_converter

import androidx.room.TypeConverter
import com.lianyi.paimonsnotebook.common.database.daily_note.entity.DailyNote
import com.lianyi.paimonsnotebook.common.database.user.type_converter.ITypeConverter
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData

class DailyNoteConverter:ITypeConverter<DailyNoteData,String> {

    @TypeConverter
    override fun convertToEntityProperty(databaseValue: String): DailyNoteData = JSON.parse(databaseValue)

    @TypeConverter
    override fun convertToDatabaseValue(entity: DailyNoteData): String = JSON.stringify(entity)
}