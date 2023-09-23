package com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.event.gacha_info

import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.ui.screen.gacha.util.UIGFHelper

data class GachaLogData(
    val list: List<Item>,
    val page: String,
    val region: String,
    val size: String,
    val total: String,
) {
    data class Item(
        val count: String,
        val gacha_type: String,
        val id: String,
        val item_id: String,
        val item_type: String,
        val lang: String,
        val name: String,
        val rank_type: String,
        val time: String,
        val uid: String,
    ) {
        fun asGachaItems(itemId:String = item_id) = GachaItems(
            count = count,
            gacha_type = gacha_type,
            id = id,
            item_id = itemId,
            item_type = item_type,
            name = name,
            rank_type = rank_type,
            time = time,
            uigf_gacha_type = UIGFHelper.getUIGFType(gacha_type),
            uid = uid,
            lang = lang
        )
    }
}