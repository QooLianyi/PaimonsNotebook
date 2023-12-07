package com.lianyi.paimonsnotebook.common.database.gacha.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.lianyi.paimonsnotebook.ui.screen.gacha.util.GachaRecordCardDisplayState
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper
import com.lianyi.paimonsnotebook.ui.theme.GachaStar3Color
import com.lianyi.paimonsnotebook.ui.theme.GachaStar4Color2
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color
import com.lianyi.paimonsnotebook.ui.theme.Primary

/*
* 祈愿记录总览
* */
data class GachaRecordOverview(
    val uid: String,
    val list: List<Item>
) {

    /*
    * uigfGachaType: 祈愿类型
    * gachaTimesMap: 已经抽取但还没有出指定星级的次数 key = star
    * countMap: 次数map key = star
    * gachaProgressMap: 抽卡进度,根据指定类型的保底数 / gachaTimes得到的一个[0,1]的数 key = star
    * maxTime: 对应类型最后抽卡的时间
    * minTime: 对应类型第一次抽卡的时间
    * */
    data class Item(
        val uigfGachaType: String,
        val gachaTimesMap: Map<Int, Int>,
        val countMap: Map<Int, Int>,
        val gachaProgressMap: Map<Int, Float>,
        val maxTime: String,
        val minTime: String
    ) {
        val uigfGachaTypeName: String
            get() = UIGFHelper.getUIGFName(uigfGachaType)

        val totalCount: Int
            get() = countMap.values.sum()

        val colorMap by lazy {
            mapOf(
                3 to GachaStar3Color,
                4 to GachaStar4Color2,
                5 to GachaStar5Color
            )
        }

        val ringChartValues: Array<Pair<Int, Color>>
            get() =
                countMap.keys.map {
                    val color = colorMap[it] ?: Primary
                    (countMap[it] ?: 0) to color
                }.toTypedArray()

        var cardDisplayState by mutableStateOf(GachaRecordCardDisplayState.None)

        var hideCardInfo by mutableStateOf(false)
    }
}
