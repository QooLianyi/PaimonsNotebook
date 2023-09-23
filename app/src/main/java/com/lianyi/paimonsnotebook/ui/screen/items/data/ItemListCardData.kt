package com.lianyi.paimonsnotebook.ui.screen.items.data

import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.QualityType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData

data class ItemListCardData(
    val name: String = "",
    val iconUrl: String = "",
    val quality: Int = 0,
    val description: String = ""
) {
    val qualityBgResId:Int
        get() = QualityType.getQualityBgByType(quality)

    companion object {
        fun fromAvatar(avatar: AvatarData) =
            ItemListCardData(
                name = avatar.name,
                iconUrl = avatar.iconUrl,
                quality = avatar.quality,
                description = avatar.description
            )

        fun fromWeapon(weapon: WeaponData) =
            ItemListCardData(
                name = weapon.name,
                iconUrl = weapon.iconUrl,
                quality = weapon.rankLevel,
                description = weapon.description
            )
    }
}
