package com.lianyi.paimonsnotebook.common.database.disk_cache.util

/*
* 硬盘缓存存储类型
* */
enum class DiskCacheDataType {
    //临时,如首页公告图片,当公告公示期结束时不会再被调用到的图片
    Temp,

    //持久,如武器图标技能图标等随时可能使用到的图片
    Stable,

    //UI,用于显示正常的界面,此类一般不能删除。
    UI
}