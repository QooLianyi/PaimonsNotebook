package com.lianyi.paimonsnotebook.common.database.gacha.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/*
* 祈愿记录实体表
* */
@Entity("gacha_items", indices = [
    Index(
        "uigf_gacha_type",
        "rank_type",
        "uid",
        name = "index_gacha_record_overview"
    )
])
data class GachaItems(
    val count: String,
    val gacha_type: String,
    @PrimaryKey(autoGenerate = false)
    val id: String,
    val item_id: String,
    val item_type: String,
    val lang: String,
    val name: String,
    val rank_type: String,
    val time: String,
    val uid: String,
    val uigf_gacha_type: String
)
