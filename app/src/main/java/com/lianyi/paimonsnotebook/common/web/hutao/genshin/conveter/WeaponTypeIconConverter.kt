package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.WeaponType

object WeaponTypeIconConverter {

    fun weaponTypeToIconUrl(type: Int): String {
        val weapon = when (type) {
            WeaponType.WEAPON_SWORD_ONE_HAND -> "01"
            WeaponType.WEAPON_BOW -> "02"
            WeaponType.WEAPON_POLE -> "03"
            WeaponType.WEAPON_CLAYMORE -> "04"
            WeaponType.WEAPON_CATALYST -> "Catalyst_MD"
            else -> ""
        }
        return HutaoEndpoints.staticFile("Skill", "Skill_A_${weapon}.png")
    }
}