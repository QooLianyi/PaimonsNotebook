package com.lianyi.paimonsnotebook.common.database.disk_cache.dao

import androidx.room.*
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import kotlinx.coroutines.flow.Flow

@Dao
interface DiskCacheDao {

    @Upsert
    fun insert(data: DiskCache)

    @Query("select * from disk_cache order by create_time desc")
    fun getAllData(): Flow<List<DiskCache>>

    @Query("select * from disk_cache where plan_delete = 0 order by create_time desc")
    fun getData(): Flow<List<DiskCache>>

    @Query("select * from disk_cache where url = :url")
    fun getDataByUrl(url: String): DiskCache

    @Query("select * from disk_cache limit :currentPage,:pageCount")
    fun getDataByPage(currentPage: Int, pageCount: Int): Flow<List<DiskCache>>

    @Query("select * from disk_cache where plan_delete = 1")
    fun getPlanDeleteData(): Flow<List<DiskCache>>

    //标记列表中的url
    @Query("update disk_cache set plan_delete = :targetValue where url in (:urls)")
    fun setPlanDeleteFlag(urls: List<String>,targetValue:Int)

    @Query("update disk_cache set plan_delete = :targetValue where url = :url")
    fun setPlanDeleteFlag(url: String,targetValue:Int)

    @Query("delete from disk_cache where url = :url")
    fun deleteByUrl(url: String)

    @Query("delete from disk_cache where url in (:urls)")
    fun deleteByUrls(urls: List<String>)

    @Query("select count(*) from disk_cache where url = :url")
    fun queryDataCountByUrl(url: String): Int

    @Query("update disk_cache set last_use_time = :last_use_time , use_count = use_count + 1 , last_use_from = :lastUseFrom  where url = :url")
    fun updateUseInfo(url: String, lastUseFrom: String, last_use_time: Long = 0L)

    /*
    * 更新所有数据的计划删除状态
    *
    * 将临时过期图片设置为计划删除状态
    * */
    @Query("update disk_cache set plan_delete = 1 where last_use_time + :limitTime < :currentTime and type = 'Temp'")
    fun updateAllDataPlanDeleteStatus(currentTime: Long, limitTime: Long): Int

    //删除所有计划删除数据
    @Query("delete from disk_cache where plan_delete = 1")
    fun removeAllPlanDeleteData()

    @Query("update disk_cache set plan_delete = :status where url in (:urls)")
    fun updatePlanDeleteStatusByUrls(urls: List<String>, status: Int)

}