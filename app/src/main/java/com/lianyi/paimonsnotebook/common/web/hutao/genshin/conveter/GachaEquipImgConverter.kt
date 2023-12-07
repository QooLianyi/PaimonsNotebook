package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.static_resources.StaticResourcesApiEndpoint


object GachaEquipImgConverter {
    fun iconNameToUrl(name: String) = StaticResourcesApiEndpoint.staticRaw(
        "GachaEquipIcon",
        "UI_Gacha_EquipIcon_${name.removePrefix("UI_EquipIcon_")}.png"
    )
}