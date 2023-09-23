package com.lianyi.paimonsnotebook.common.database.test.cross_ref

import androidx.room.Embedded
import androidx.room.Relation
import com.lianyi.paimonsnotebook.common.database.test.entity.TestCard
import com.lianyi.paimonsnotebook.common.database.test.entity.TestRole
import com.lianyi.paimonsnotebook.common.database.test.entity.TestUser

data class TestUserAndCard(
    @Embedded
    val user: TestUser,
    @Relation(parentColumn = "id", entityColumn = "userId")
    val card: TestCard
)