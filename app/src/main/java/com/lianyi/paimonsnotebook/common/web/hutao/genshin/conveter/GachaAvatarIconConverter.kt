package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.static_resources.StaticResourcesApiEndpoint

object GachaAvatarIconConverter {
    fun iconNameToUrl(name: String) = StaticResourcesApiEndpoint.staticRaw(
        "GachaAvatarIcon",
        "UI_Gacha_AvatarIcon_${name.removePrefix("UI_AvatarIcon_")}.png"
    )
}