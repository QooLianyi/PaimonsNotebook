package com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter

import com.lianyi.paimonsnotebook.common.web.HutaoEndpoints

object AvatarNameCardPicConverter {
    fun iconNameToUrl(name: String) = HutaoEndpoints.staticFile(
        "NameCardPic",
        "UI_NameCardPic_${replaceSpecialCaseNaming(name.removePrefix("UI_AvatarIcon_"))}_P.png"
    )

    private fun replaceSpecialCaseNaming(name: String) =
        when (name) {
            "Yae" -> "Yae1"
            "Momoka" -> "Kirara"
            else -> name
        }

}