package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate

data class BatchCalculatePromotionDetail(
    val items: List<Item>,
    val region: String,
    val uid: String
) {
    data class Item(
        val avatar_id: Int? = null,
        val avatar_level_current: Int? = null,
        val avatar_level_target: Int? = null,
        val element_attr_id: Int? = null,
        val skill_list: List<Skill>? = null,
        val weapon: Weapon? = null
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

