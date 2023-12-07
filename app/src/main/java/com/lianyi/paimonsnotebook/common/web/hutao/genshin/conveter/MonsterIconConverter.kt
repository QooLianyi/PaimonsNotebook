package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.static_resources.StaticResourcesApiEndpoint


object MonsterIconConverter {
    fun iconNameToUrl(name: String) = StaticResourcesApiEndpoint.staticRaw(
        "MonsterIcon",
        "${name}.png"
    )
}