package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object GachaEquipImgConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        "GachaEquipIcon",
        "UI_Gacha_EquipIcon_${name.removePrefix("UI_EquipIcon_")}.png"
    )
}