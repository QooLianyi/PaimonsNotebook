package com.lianyi.paimonsnotebook.common.database.test.entity

import androidx.room.*

@Entity(
    "test_card", foreignKeys = [
        ForeignKey(
            entity = TestUser::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class TestCard(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(index = true)
    val userId: Long,
    val cardContent: String,
)
