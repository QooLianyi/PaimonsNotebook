package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.event.calculate

data class BatchComputeData(
    val available_material: List<Any>,
    val has_user_info: Boolean,
    val items: List<Item>,
    //包含所有计算项所有材料的缺少数量
    val overall_consume: List<OverallConsume>,
    val overall_material_consume: OverallMaterialConsume
) {

    data class Item(
        val avatar_consume: List<AvatarConsume>,
        val avatar_skill_consume: List<AvatarSkillConsume>,
        val calendar: Calendar,
        val lineup_recommend: String,
        val reliquary_consume: List<Any>,
        val skills_consume: List<SkillsConsume>,
        val weapon_consume: List<WeaponConsume>
    )

    data class OverallConsume(
        val icon: String,
        val icon_url: String,
        val id: Int,
        val lack_num: Int,
        val level: Int,
        val name: String,
        val num: Int,
        val wiki_url: String
    )

    data class OverallMaterialConsume(
        val avatar_consume: List<AvatarConsumeX>,
        val avatar_skill_consume: List<AvatarSkillConsumeX>,
        val weapon_consume: List<WeaponConsumeX>
    )

    data class AvatarConsume(
        val icon: String,
        val icon_url: String,
        val id: Int,
        val lack_num: Int,
        val level: Int,
        val name: String,
        val num: Int,
        val wiki_url: String
    )

    data class AvatarSkillConsume(
        val icon: String,
        val icon_url: String,
        val id: Int,
        val lack_num: Int,
        val level: Int,
        val name: String,
        val num: Int,
        val wiki_url: String
    )

    data class Calendar(
        val calendar_link: String,
        val drop_day: List<String>,
        val dungeon_name: String,
        val has_data: Boolean
    )

    data class SkillsConsume(
        val consume_list: List<Consume>,
        val skill_info: SkillInfo
    )

    data class WeaponConsume(
        val icon: String,
        val icon_url: String,
        val id: Int,
        val lack_num: Int,
        val level: Int,
        val name: String,
        val num: Int,
        val wiki_url: String
    )

    data class Consume(
        val icon: String,
        val icon_url: String,
        val id: Int,
        val lack_num: Int,
        val level: Int,
        val name: String,
        val num: Int,
        val wiki_url: String
    )

    data class SkillInfo(
        val id: String,
        val level_current: String,
        val level_target: String
    )

    data class AvatarConsumeX(
        val avatars: List<Avatar>,
        val consume: List<Consume>,
        val weapons: List<Any>
    )

    data class AvatarSkillConsumeX(
        val avatars: List<Avatar>,
        val consume: List<Consume>,
        val weapons: List<Any>
    )

    data class WeaponConsumeX(
        val avatars: List<Any>,
        val consume: List<Consume>,
        val weapons: List<Weapon>
    )

    data class Avatar(
        val avatar_level: Int,
        val icon: String,
        val id: Int
    )

    data class Weapon(
        val icon: String,
        val id: Int,
        val weapon_level: Int
    )
}