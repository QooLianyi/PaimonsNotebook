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

    @Query("select * from disk_cache limit :currentPage,:pageCount")
    fun getDataByPage(currentPage: Int, pageCount: Int): Flow<List<DiskCache>>

    @Query("select * from disk_cache where plan_delete = 1")
    fun getPlanDeleteData(): Flow<List<DiskCache>>

    @Query("delete from disk_cache where url = :url")
    fun deleteByUrl(url: String)

    @Query("delete from disk_cache where url in (:urls)")
    fun deleteByUrls(urls: List<String>)

    @Query("delete from disk_cache where plan_delete = 1")
    fun removePlanDelete()

    @Query("select count(*) from disk_cache where url = :url")
    fun queryDataCountByUrl(url: String): Int

    @Query("update disk_cache set last_use_time = :last_use_time , use_count = use_count + 1 , last_use_from = :lastUseFrom  where url = :url")
    fun updateUseInfo(url: String, lastUseFrom: String, last_use_time: Long = 0L)

    @Query("update disk_cache set plan_delete = :status where url = :url")
    fun updatePlanDeleteStatus(url: String, status: Int)

    @Query("update disk_cache set plan_delete = :status where url in (:urls)")
    fun updatePlanDeleteStatusByUrls(urls: List<String>, status: Int)

}