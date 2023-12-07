package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.static_resources.StaticResourcesApiEndpoint


object ItemIconConverter {
    fun iconNameToUrl(name: String) = if (name.startsWith("UI_RelicIcon_")) {
        RelicIconConverter.iconNameToUrl(name)
    } else {
        StaticResourcesApiEndpoint.staticRaw(
            "ItemIcon",
            "${name}.png"
        )
    }
}