package com.lianyi.paimonsnotebook.common.database.disk_cache.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.util.time.TimeStampType

/*
* 本地缓存的图片
* url:图片url,主键
* name:图片名称,
* description:图片描述
* createFrom:图片来源
* lastUseTime:最后使用的时间
* type:缓存类型
* createTime:创建时间
* useCount:使用次数
* lastUseFrom:最后调用处
* */
@Entity(tableName = "disk_cache")
data class DiskCache(
    @PrimaryKey(autoGenerate = false)
    val url: String,
    val name: String = "",
    val description: String = "",
    @ColumnInfo(name = "create_from")
    val createFrom: String = "",
    @ColumnInfo(name = "last_use_time")
    val lastUseTime: Long = System.currentTimeMillis(),
    val type: DiskCacheDataType = DiskCacheDataType.Temp,
    @ColumnInfo(name = "create_time")
    val createTime: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "use_count")
    val useCount: Int = 1,
    @ColumnInfo(name = "last_use_from")
    val lastUseFrom: String = "",
    @ColumnInfo(name = "plan_delete")
    val planDelete: Int = 0,
) {

    val createTimeFormat
        get() = TimeHelper.getTime(createTime, TimeStampType.YYYY_MM_DD_HH_MM_SS)
    val lastUseTimeFormat
        get() = TimeHelper.getTime(lastUseTime, TimeStampType.YYYY_MM_DD_HH_MM_SS)

    val typeName
        get() = when (type) {
            DiskCacheDataType.Temp -> "临时"
            DiskCacheDataType.Stable -> "长期"
            else -> "默认"
        }
}
