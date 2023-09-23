package com.lianyi.paimonsnotebook.common.database.test.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class TestRole(
    @PrimaryKey
    @ColumnInfo("role_uid")
    val roleUid:String,
    @ColumnInfo("user_id")
    val userId:Long,
    @ColumnInfo("role_info")
    val roleInfo:String
)
