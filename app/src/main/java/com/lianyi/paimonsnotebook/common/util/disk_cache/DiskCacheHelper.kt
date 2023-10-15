package com.lianyi.paimonsnotebook.common.util.disk_cache

import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType

/*
* 管理所有的图片缓存数据
* */
object DiskCacheHelper {

    fun getDefault(url: String) =
        DiskCache(
            url = url
        )

    fun getMetadataDiskCache(url: String) =
        DiskCache(
            url = url,
            type = DiskCacheDataType.Stable,
            name = "元数据图片",
            description = "用于展示相关信息的图片"
        )

}