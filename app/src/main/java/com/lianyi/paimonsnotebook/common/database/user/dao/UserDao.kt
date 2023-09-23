package com.lianyi.paimonsnotebook.common.database.user.dao

import androidx.room.*
import com.lianyi.paimonsnotebook.common.database.user.entity.User
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {

    @Upsert
    fun insert(user: User)

    @Update
    fun update(user: User): Int

    @Query("select * from users where is_selected = 1 limit 1")
    fun getSelectedUser(): Flow<User?>

    @Query("update users set is_selected = 0")
    fun unSelectAllUser()

    @Query("update users set is_selected = :value where mid = :mid")
    fun updateUserSelectState(value: Int, mid: String)

    @Query("select * from users")
    fun getUserListFlow(): Flow<List<User>>

    @Query("select * from users")
    fun getAllUser(): List<User>

    @Query("select * from users where mid = :mid")
    fun getUserByMid(mid: String): User?

    @Delete
    fun delete(user: User): Int
}