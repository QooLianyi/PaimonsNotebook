package com.lianyi.paimonsnotebook.common.database.cultivate.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
* 养成计划档案表
*
* */
@Entity("cultivate_project")
data class CultivateProject(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("project_id")
    val projectId: Int,
    @ColumnInfo("project_name")
    val projectName:String
)