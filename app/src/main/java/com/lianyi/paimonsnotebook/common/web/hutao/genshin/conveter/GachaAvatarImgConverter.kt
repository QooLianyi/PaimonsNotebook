package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.static_resources.StaticResourcesApiEndpoint


object GachaAvatarImgConverter {
    fun iconNameToUrl(name: String) = StaticResourcesApiEndpoint.staticRaw(
        "GachaAvatarImg",
        "UI_Gacha_AvatarImg_${name.removePrefix("UI_AvatarIcon_")}.png"
    )
}