package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate

data class AvatarPromotionDelta(
    val avatar_id: Int,
    val avatar_level_current: Int,
    val avatar_level_target: Int,
    val element_attr_id: Int,
    val skill_list: List<Skill>,
    val weapon: Weapon?
){
    companion object{
        //空模板
        val emptyTemplate by lazy {
            AvatarPromotionDelta(
                avatar_id = 10000000,
                avatar_level_target = 1,
                avatar_level_current = 1,
                element_attr_id = 0,
                skill_list = listOf(),
                weapon = null
            )
        }
    }

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