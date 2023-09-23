package com.lianyi.paimonsnotebook.common.database.gacha.data

import androidx.room.ColumnInfo

data class WishHistoryItem(
    @ColumnInfo("uigf_gacha_type")
    val uigfGachaType:String,
    @ColumnInfo("rank_type")
    val rankType:String,
    val id:String,
    val name:String,
    @ColumnInfo("item_type")
    val itemType:String,
)
