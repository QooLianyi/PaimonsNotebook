package com.lianyi.paimonsnotebook.common.database.achievement.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey


/*
* 成就管理实体
*
*
* timestamp:完成时间(秒)
*
* 添加外键,外键关联成就管理用户表
* 更新/删除同步进行
*
* 添加主键,主键为成就id+用户id
* */
@Entity(
    "achievements",
    foreignKeys = [
        ForeignKey(
            entity = AchievementUser::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("user_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ], primaryKeys = ["id", "user_id"]
)
data class Achievements(
    val id: Int,
    val current: Int,
    val status: Int,
    val timestamp: Long,
    @ColumnInfo("user_id")
    val userId: Int
)