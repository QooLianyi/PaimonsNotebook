package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object GachaAvatarIconConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        "GachaAvatarIcon",
        "UI_Gacha_AvatarIcon_${name.removePrefix("UI_AvatarIcon_")}.png"
    )
}