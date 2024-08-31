package com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary

data class ReliquaryData(
    val Description: String,
    val EquipType: Int,
    val Icon: String,
    val Ids: List<Int>,
    val ItemType: Int,
    val Name: String,
    val RankLevel: Int,
    val SetId: Int
)