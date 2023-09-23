package com.lianyi.paimonsnotebook.common.database.gacha.data

import androidx.room.ColumnInfo

/*
* 祈愿记录总览查询结果Item
*
* uigfGachaType:祈愿类型
* gachaTimes:当前星级在当前卡池类型中距离上一个同星级的个数
* rankType:星级
* minTime:第一次抽卡的时间
* maxTime:最后一次抽卡的时间
* uid:uid
* count:当前星级在当前卡池类型中占的总个数
* */
data class GachaRecordOverviewItem(
    @ColumnInfo("uigf_gacha_type")
    val uigfGachaType: String,
    @ColumnInfo("gacha_times")
    val gachaTimes:Int,
    @ColumnInfo("rank_type")
    val rankType: String,
    @ColumnInfo("min_time")
    val minTime:String,
    @ColumnInfo("max_time")
    val maxTime:String,
    val uid: String,
    val count: Int,
)
