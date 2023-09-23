package com.lianyi.paimonsnotebook.common.database.daily_note.entity

import androidx.room.*
import com.lianyi.paimonsnotebook.common.database.daily_note.type_converter.DailyNoteConverter
import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData

/*
* 实时便笺
* 外键约束user的mid(主键)
* 更新与删除同步进行
* */

@Entity(
    "daily_note",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("mid"),
        childColumns = arrayOf("user_mid"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
@TypeConverters(DailyNoteConverter::class)
data class DailyNote(
    @PrimaryKey
    val uid: String,
    @ColumnInfo("user_mid", index = true)
    val userMid: String,
    @ColumnInfo("daily_note")
    val dailyNote: DailyNoteData,
    val sort: Int,
)
