package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object GachaAvatarImgConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        "GachaAvatarImg",
        "UI_Gacha_AvatarImg_${name.removePrefix("UI_AvatarIcon_")}.png"
    )
}