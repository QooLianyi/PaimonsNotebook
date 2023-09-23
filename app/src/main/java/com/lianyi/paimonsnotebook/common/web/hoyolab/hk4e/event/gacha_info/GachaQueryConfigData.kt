package com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.event.gacha_info

import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.GameAuthKeyData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.GenAuthKeyData

data class GachaQueryConfigData(
    val gachaType: String,
    val gameAuthKeyData: GameAuthKeyData,
    val genAuthKeyData: GenAuthKeyData,
    val size: Int = 20,
    var endId: String = "0",
    val lang: String = "zh-cn",
) {
    val asQueryParameter: String
        get() = "size=${size}&end_id=${endId}&lang=${lang}&gacha_type=${gachaType}&${genAuthKeyData.asQueryParameter}&${gameAuthKeyData.asQueryParameter}"
}
