package com.lianyi.paimonsnotebook.common.database.gacha.dao

import androidx.room.*
import com.lianyi.paimonsnotebook.common.database.gacha.data.GachaRecordOverviewItem
import com.lianyi.paimonsnotebook.common.database.gacha.data.WishHistoryItem
import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import kotlinx.coroutines.flow.Flow

@Dao
interface GachaItemsDao {

    //插入数据,主键重复则替换
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gachaLogItem: GachaItems)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<GachaItems>)

    //更新数据
    @Update
    fun update(gachaLogItem: GachaItems)

    //删除数据
    @Delete
    fun delete(gachaLogItem: GachaItems): Int

    //根据UID删除记录
    @Query("delete from gacha_items where uid = :uid")
    fun deleteGachaLogItemByUid(uid: String)

    //删除所有数据 测试用
    @Query("delete from gacha_items where 1=1")
    fun deleteAllGachaLogItem()

    //根据UID查询记录
    @Query("select * from gacha_items where uid = :uid order by id")
    fun getGachaLogItemByUidFlow(uid: String): Flow<List<GachaItems>>

    @Query("select * from gacha_items where uid = :uid order by id")
    fun getGachaLogItemByUid(uid: String): List<GachaItems>

    /*
    * 分页查询
    * count:页数
    * page:页码,从0开始
    * */
    @Query("select * from gacha_items where uid = :uid order by id limit :pageSize offset :pageSize * :page")
    fun getGachaLogItemByUidPage(uid: String, page: Int, pageSize: Int): List<GachaItems>

    //根据UID与卡池ID查询记录
    @Query("select * from gacha_items where uid = :uid and uigf_gacha_type = :gachaType order by id")
    fun getGachaLogItemByUidAndGachaType(uid: String, gachaType: Int): Flow<List<GachaItems>>

    //获取全部数据
    @Query("select * from gacha_items order by id")
    fun getAllFlow(): Flow<List<GachaItems>>

    @Query("select * from gacha_items order by id")
    fun getAll(): List<GachaItems>

    @Query("select count(id) from gacha_items")
    fun getSize(): Flow<Int>

    //根据UID计算记录的条数
    @Query("select count(id) from gacha_items where uid = :uid")
    fun getGachaLogItemCountByUid(uid: String): Int

    //根据UID与卡池类型获取最后一条记录ID
    @Query("select id from gacha_items where uid = :uid and uigf_gacha_type = :gachaType order by id desc limit 1")
    fun getLastIdByUidAndUIGFGachaType(uid: String, gachaType: String): String

    //获取卡池记录中出现的UID
    @Query("select uid from gacha_items group by uid")
    fun getAllGameUidFlow(): Flow<List<String>>


    @Query("select count(*) from gacha_items where id = :id and uid = :uid")
    fun isExist(id: String,uid: String): Boolean

    /*
    * 从本地获取各个uid祈愿卡池各个星级的距离上一个记录的个数(距离下次保底的次数)
    * 有大量数据时查询耗时会增加
    * todo 记录查询sql优化
    * 一百一十三万数据的条件下,开发环境无缓存查询需要8000毫秒左右
    * 实际生产环境时间会增加
    * */
    @Query(
        """
        select gacha_items.uigf_gacha_type,count(gacha_items.uigf_gacha_type) - 1 as `gacha_times`,A.rank_type ,A.min_time,A.max_time,A.uid,A.count from 
        (
         select count(uigf_gacha_type) as `count`,uigf_gacha_type , rank_type , uid , max(id) as `max_id`,max(time) as `max_time`,min(time) as `min_time` from  gacha_items group by uigf_gacha_type , rank_type , uid
        ) A ,
        gacha_items
        where gacha_items.uigf_gacha_type = A.uigf_gacha_type and gacha_items.uid = A.uid and gacha_items.id >= max_id
        group by A.uigf_gacha_type ,A.rank_type , A.uid
    """
    )
    fun getOverviews(): List<GachaRecordOverviewItem>

    @Query(
        """
        select gacha_items.uigf_gacha_type,count(gacha_items.uigf_gacha_type) - 1 as `gacha_times`,A.rank_type ,A.min_time,A.max_time,A.uid,A.count from 
        (
         select count(uigf_gacha_type) as `count`,uigf_gacha_type , rank_type , uid , max(id) as `max_id`,max(time) as `max_time`,min(time) as `min_time` from  gacha_items where uid = :uid group by uigf_gacha_type , rank_type , uid
        ) A ,
        gacha_items
        where gacha_items.uigf_gacha_type = A.uigf_gacha_type and gacha_items.uid = A.uid and gacha_items.id >= max_id
        group by A.uigf_gacha_type ,A.rank_type , A.uid
    """
    )
    fun getOverviewsByUid(uid: String): List<GachaRecordOverviewItem>

    /*
    * 根据uid从本地获取所有祈愿记录的每一个星级的最后一条记录的id
    * */
    @Query(
        """
        select count(gacha_items.uigf_gacha_type) from gacha_items, 
        (
            select uigf_gacha_type , rank_type , uid , max(id) as `max_id` from gacha_items where uigf_gacha_type = :uigfGachaType and rank_type = :rankType and uid  = :uid limit 100
        ) A
        where gacha_items.uigf_gacha_type = A.uigf_gacha_type and gacha_items.uid = A.uid and gacha_items.id >= max_id
    """
    )
    fun getGachaRecordForwardCount(uigfGachaType: String, rankType: String, uid: String): Int

    @Query("select max(id) from gacha_items where uigf_gacha_type = :uigfGachaType and rank_type = :rankType and uid  = :uid")
    fun getGachaRecordMaxId(uigfGachaType: String, rankType: String, uid: String): String

    /*
    * 根据卡池类型、星级、UID获取对应的结果列表
    * 用于计算抽出五星所消耗的次数
    * */
    @Query("select uigf_gacha_type,rank_type,id,name,item_type from gacha_items where uigf_gacha_type = :uigfGachaType and uid = :uid order by id")
    fun getHistoryWishByUIGFGachaTypeAndUid(uigfGachaType: String, uid:String):List<WishHistoryItem>


    /*
    * 通知room gacha_items表更新
    *
    * 更新一个不存在的id的数据来触发room更新
    * 调用原生的插入方法并不会触发room的更新,因此需要手动来通知
    * */
    @Query(
        "update gacha_items set gacha_type = gacha_type where '-1' = '-1'"
    )
    fun notifyRoomGachaItemsUpdate()
}