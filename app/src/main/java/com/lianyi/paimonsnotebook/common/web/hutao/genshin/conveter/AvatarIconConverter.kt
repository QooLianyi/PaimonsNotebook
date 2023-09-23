package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object AvatarIconConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        "AvatarIcon",
        "${name}.png"
    )
}