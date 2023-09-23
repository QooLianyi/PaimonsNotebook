package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format

import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.SkillIconConverter
import kotlin.math.roundToInt

/*
* 角色技能格式化
* */
data class AvatarSkillFormat(
    val name: String,
    val description: String,
    val subName: String,
    val icon: String,
    val maxLevel: Int,
    val proud: AvatarData.Proud
):BaseFormat() {
    companion object {
        //通过命座获取
        fun getValueForTalent(talent: AvatarData.Talent, subName: String) =
            AvatarSkillFormat(
                name = talent.Name,
                description = talent.Description,
                subName = subName,
                icon = talent.Icon,
                maxLevel = 1,
                proud = AvatarData.Proud(listOf(), listOf())
            )

        //通过技能获取
        fun getValueForSkill(skill: AvatarData.Skill, skillTypeName: String) =
            AvatarSkillFormat(
                name = skill.Name,
                description = skill.Description,
                icon = skill.Icon,
                maxLevel = skill.Proud.Parameters.size,
                subName = skillTypeName,
                proud = skill.Proud
            )

        //通过元素爆发技能获取
        fun getValueForEnergySkill(energySkill: AvatarData.EnergySkill) =
            AvatarSkillFormat(
                name = energySkill.Name,
                description = energySkill.Description,
                icon = energySkill.Icon,
                maxLevel = energySkill.Proud.Parameters.size,
                subName = "元素爆发",
                proud = energySkill.Proud
            )

        //通过固有天赋技能获取
        fun getValueForInherent(inherent: AvatarData.Inherent) =
            AvatarSkillFormat(
                name = inherent.Name,
                description = inherent.Description,
                icon = inherent.Icon,
                maxLevel = inherent.Proud.Parameters.size,
                subName = "固有天赋",
                proud = inherent.Proud
            )
    }

    val iconUrl: String
        get() = SkillIconConverter.iconNameToUrl(name = icon)

    fun setLevel(level: Float) {
        currentLevel = if (level >= maxLevel) {
            maxLevel
        } else {
            level.roundToInt()
        }
    }
}