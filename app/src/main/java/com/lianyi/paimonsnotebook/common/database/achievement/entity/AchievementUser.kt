package com.lianyi.paimonsnotebook.common.database.achievement.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


/*
* 成就记录用户
*
* selected:是否被选中
* */
@Entity("achievement_user")
data class AchievementUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val selected: Int
)