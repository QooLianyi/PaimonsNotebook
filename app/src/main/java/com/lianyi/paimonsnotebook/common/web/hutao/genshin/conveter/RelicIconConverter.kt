package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object RelicIconConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        "RelicIcon",
        "${name}.png"
    )
}