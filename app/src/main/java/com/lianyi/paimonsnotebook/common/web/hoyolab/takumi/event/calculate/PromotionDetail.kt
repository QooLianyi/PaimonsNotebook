package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate

data class PromotionDetail(
    val avatar_id: Int?,
    val avatar_level_current: Int?,
    val avatar_level_target: Int?,
    val element_attr_id: Int?,
    val skill_list: List<Skill>?,
    val weapon: Weapon? = null,
    val reliquary_list: List<Reliquary> = listOf()
) {
    data class Reliquary(
        val id: Int,
        val level_current: Int,
        val level_target: Int
    )

    data class Skill(
        val id: Int,
        val level_current: Int,
        val level_target: Int
    )

    data class Weapon(
        val id: Int,
        val level_current: Int,
        val level_target: Int
    )
}