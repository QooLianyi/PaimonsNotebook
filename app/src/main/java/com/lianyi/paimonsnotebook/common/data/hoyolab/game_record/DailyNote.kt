package com.lianyi.paimonsnotebook.common.data.hoyolab.game_record

import com.lianyi.paimonsnotebook.common.database.user.entity.User as UserEntity
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.database.daily_note.entity.DailyNote as dailyNoteEntity

data class DailyNote(
    val dailyNoteEntity: dailyNoteEntity,
    val userEntity: UserEntity,
    val role: UserGameRoleData.Role,
)
