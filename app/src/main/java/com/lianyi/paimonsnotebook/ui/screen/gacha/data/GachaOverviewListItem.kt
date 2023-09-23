package com.lianyi.paimonsnotebook.ui.screen.gacha.data

import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.QualityType

data class GachaOverviewListItem(
    val name: String,
    val iconUrl: String,
    val rankType: Int,
    val count: Int,
    val type: String
) {
    val bgResId: Int
        get() = QualityType.getQualityBgByType(rankType)
}
