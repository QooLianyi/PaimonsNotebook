package com.lianyi.paimonsnotebook.common.database.daily_note.dao

import androidx.room.*
import com.lianyi.paimonsnotebook.common.database.daily_note.entity.DailyNote
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyNoteDao {
    @Upsert
    fun insert(dailyNote: DailyNote)

    @Update
    fun update(dailyNote: DailyNote)

    @Delete
    fun delete(dailyNote: DailyNote)

    @Query("select * from daily_note")
    fun getDailyNoteData(): Flow<List<DailyNote>>

    @Query("select * from daily_note where uid = :uid")
    fun getDailyNoteByGameRoleUid(uid: String): DailyNote?
}