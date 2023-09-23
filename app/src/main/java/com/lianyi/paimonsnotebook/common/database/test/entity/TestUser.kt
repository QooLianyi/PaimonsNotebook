package com.lianyi.paimonsnotebook.common.database.test.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("test_user")
data class TestUser(
    val name:String,
    @PrimaryKey(autoGenerate = true)
    val id:Long
)