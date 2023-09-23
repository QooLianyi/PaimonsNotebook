package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object ItemIconConverter {
    fun iconNameToUrl(name: String) = if (name.startsWith("UI_RelicIcon_")) {
        RelicIconConverter.iconNameToUrl(name)
    } else {
        HutaoEndpoints.staticFile(
            "ItemIcon",
            "${name}.png"
        )
    }
}