package com.lianyi.paimonsnotebook.common.database.test.dao

import androidx.room.*
import com.lianyi.paimonsnotebook.common.database.test.cross_ref.TestUserAndCard
import com.lianyi.paimonsnotebook.common.database.test.entity.TestCard
import com.lianyi.paimonsnotebook.common.database.test.entity.TestRole
import com.lianyi.paimonsnotebook.common.database.test.entity.TestUser
import kotlinx.coroutines.flow.Flow

@Dao
interface TestDao {

    @Upsert
    fun addCard(card:TestCard)

    @Update
    fun updateCard(card: TestCard)

    @Upsert
    fun addUser(user:TestUser)

    @Delete
    fun deleteUser(user: TestUser)

    @Transaction
    @Query("select * from test_user")
    fun getUserAndCard():List<TestUserAndCard>

}