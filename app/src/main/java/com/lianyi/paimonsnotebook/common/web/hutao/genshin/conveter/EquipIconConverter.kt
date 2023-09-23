package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object EquipIconConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        "EquipIcon",
        "${name}.png"
    )
}