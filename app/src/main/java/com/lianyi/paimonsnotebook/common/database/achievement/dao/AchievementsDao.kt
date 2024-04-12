package com.lianyi.paimonsnotebook.common.database.achievement.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.lianyi.paimonsnotebook.common.database.achievement.data.AchievementUserOverviewData
import com.lianyi.paimonsnotebook.common.database.achievement.entity.Achievements
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievementsDao {


    @Update
    fun update(list: List<Achievements>)

    @Update
    fun update(achievements: Achievements)

    @Delete
    fun delete(achievements: Achievements)

    @Delete
    fun delete(list: List<Achievements>)

    @Upsert
    fun insert(list:List<Achievements>)

    @Upsert
    fun insert(achievements: Achievements)

    //根据用户id获取成就列表
    @Query(
        "select * from achievements where user_id = :userId"
    )
    fun getAchievementListByUserId(userId: Int): Flow<List<Achievements>>

    //根据用户id获取成就列表流
    @Query(
        "select * from achievements where user_id = :userId"
    )
    fun getAchievementListByUserIdFlow(userId: Int): List<Achievements>

    /*
    * 通过成就id集合与用户id,获取数据库中的个数
    *
    * 调用此方法时应注意ids长度不能为大于999 - 其他变量占位符的个数,否则会导致解析失败,这是sqlite的硬性规定
    * */
    @Query(
        "select count(id) from achievements where user_id = :userId and id in(:ids)"
    )
    fun getCountByAchievementIdsAndUserId(ids: List<Int>, userId: Int): Int


    /*
    * 通过用户id与成就id集合获取完成的成就
    *
    * status为3或2的成就数量,3 = 已领取的成就 2 = 完成的成就
    *
    * 调用此方法时应注意ids长度不能为大于999 - 其他变量占位符的个数,否则会导致解析失败,这是sqlite的硬性规定
    * */
    @Query(
        "select * from achievements where user_id = :userId and id in(:ids) and (status = 3 or status = 2)"
    )
    fun getAchievementListByUserIdAndIds(userId: Int, ids: List<Int>): List<Achievements>

    /*
    * 分页查询
    * count:页数
    * page:页码,从0开始
    * */
    @Query("select * from achievements where user_id = :userId order by id limit :pageSize offset :pageSize * :page")
    fun getAchievementsByUserIdPage(userId: Int, page: Int, pageSize: Int): List<Achievements>

    //删除全部数据,
//    @Query("delete from achievements where 1 = 1")
//    fun deleteAllAchievement()

    /*
    * 获取所有用户的成就完成情况
    *
    * key = user_id,value = status为3或2的成就数量,3 = 已领取的成就 2 = 完成的成就
    * */
    @Query(
        "select user_id as `userId`,count(id) as `finishCount` from achievements where status = 3 or status = 2 group by user_id"
    )
    fun getAllUserAchievementOverviewFlow(): Flow<List<AchievementUserOverviewData>>

}