package com.lianyi.paimonsnotebook.common.web.hutao.genshin.item

import com.lianyi.paimonsnotebook.common.data.popup.IconTitleInformationPopupWindowData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.ItemIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.QualityType

data class Material(
    val Description: String,
    val EffectDescription: String?,
    val Icon: String,
    val Id: Int,
    val ItemType: Int,
    val MaterialType: Int,
    val Name: String,
    val RankLevel: Int,
    val TypeDescription: String
) {
    val qualityResId: Int
        get() = QualityType.getQualityBgByType(RankLevel)

    val iconUrl: String
        get() = ItemIconConverter.iconNameToUrl(Icon)

    fun getShowPopupWindowInfo() =
        IconTitleInformationPopupWindowData(
            title = Name,
            subTitle = TypeDescription,
            iconUrl = iconUrl,
            content = Description
        )

}