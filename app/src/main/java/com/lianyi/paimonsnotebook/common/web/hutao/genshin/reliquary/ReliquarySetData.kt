package com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary

data class ReliquarySetData(
    val Descriptions: List<String>,
    val EquipAffixId: Int,
    val EquipAffixIds: List<Int>,
    val Icon: String,
    val Name: String,
    val NeedNumber: List<Int>,
    val SetId: Int
)