package com.lianyi.paimonsnotebook.common.database.achievement.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.lianyi.paimonsnotebook.common.database.achievement.entity.AchievementUser
import kotlinx.coroutines.flow.Flow


/*
* 成就管理用户DAO
* */
@Dao
interface AchievementUserDao {

    @Upsert
    fun add(user: AchievementUser)

    @Delete
    fun delete(user: AchievementUser)

    @Update
    fun update(user: AchievementUser)

    /*
    * 获取选中的用户,返回用户流(可空的)
    * */
    @Query(
        "select * from achievement_user where selected = 1"
    )
    fun getSelectedUserFlow():Flow<AchievementUser?>

    @Query(
        "select * from achievement_user where selected = 1"
    )
    fun getSelectedUser():AchievementUser?

    //获取用户列表流
    @Query(
        "select * from achievement_user"
    )
    fun getUserListFlow(): Flow<List<AchievementUser>>

    @Query(
        "delete from achievement_user where id = :userId"
    )
    fun deleteById(userId: Int)

    //检查是否有相同名称的用户
    @Query(
        "select count(id) from achievement_user where name = :name"
    )
    fun checkUserExistByName(name: String): Boolean

    //删除全部用户
    @Query(
        "delete from achievement_user where 1 = 1"
    )
    fun deleteAllUser()


    /*
    * 发射当前选中用户flow
    *
    * 用于通知数据库更新选中用户的flow
    * 导入数据使用原生的插入,并不会触发room的更新检测
    * 调用该方法触发achievement_user表的更新从而使成就管理界面数据更新
    *
    * 暂时先用这个方法了
    * */
    @Query(
        "update achievement_user set selected = 1 where selected = 1"
    )
    fun emitSelectedUserFlow()
}