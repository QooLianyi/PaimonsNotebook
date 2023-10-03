package com.lianyi.paimonsnotebook.common.database.daily_note.dao

import androidx.room.*
import com.lianyi.paimonsnotebook.common.database.daily_note.entity.DailyNoteWidget

@Dao
interface DailyNoteWidgetDao {
    @Upsert
    fun upsert(dailyNote: DailyNoteWidget)

    @Update
    fun update(dailyNote: DailyNoteWidget)

    @Delete
    fun delete(dailyNote: DailyNoteWidget)

    @Query("select * from daily_note_widget where user_mid = :mid")
    fun getDailyNoteByUserMid(mid: String): DailyNoteWidget?
}