package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate

data class CalculateComputeData(
    val avatar_consume: List<ConsumeData>,
    val avatar_skill_consume: List<ConsumeData>,
    val weapon_consume: List<ConsumeData>,
//    val reliquary_consume: List<Any> //不计算圣遗物消耗
) {

    /*
    * 养成所需材料数据
    * */
    data class ConsumeData(
        val icon: String,
        val icon_url: String,
        val id: Int,
        val level: Int,
        val name: String,
        val num: Int,
    )
}