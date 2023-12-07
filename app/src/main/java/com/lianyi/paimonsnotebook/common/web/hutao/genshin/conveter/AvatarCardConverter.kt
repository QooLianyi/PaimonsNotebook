package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.static_resources.StaticResourcesApiEndpoint

object AvatarCardConverter {
    fun iconNameToUrl(name: String) = StaticResourcesApiEndpoint.staticRaw(
        "AvatarCard",
        "${name}.png"
    )
}