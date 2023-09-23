package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object AvatarCardConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        "AvatarCard",
        "${name}.png"
    )
}