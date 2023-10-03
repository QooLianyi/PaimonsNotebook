package com.lianyi.paimonsnotebook.common.database.daily_note.entity

import androidx.room.*
import com.lianyi.paimonsnotebook.common.database.daily_note.type_converter.DailyNoteWidgetConverter
import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData

/*
* 实时便笺
* 外键约束user的mid(主键)
* 更新与删除同步进行
* */

@Entity(
    "daily_note_widget",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("mid"),
        childColumns = arrayOf("user_mid"),
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )]
)
@TypeConverters(DailyNoteWidgetConverter::class)
data class DailyNoteWidget(
    @PrimaryKey
    @ColumnInfo("user_mid", index = true)
    val userMid: String,
    @ColumnInfo("daily_note_widget")
    val dailyNoteWidget: DailyNoteWidgetData,
    @ColumnInfo("update_time")
    val updateTime:Long,
)
