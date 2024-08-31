package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic

object ReliquaryType {

    /// 无
    const val EQUIP_NONE = 0

    /// 花
    const val EQUIP_BRACER = 1

    /// 羽毛
    const val EQUIP_NECKLACE = 2

    /// 沙
    const val EQUIP_SHOES = 3

    /// 杯
    const val EQUIP_RING = 4

    /// 头
    const val EQUIP_DRESS = 5

    /// 武器
    const val EQUIP_WEAPON = 6

    fun getReliquaryNameByType(type: Int) = when (type) {
        EQUIP_NONE -> "无"
        EQUIP_BRACER -> "生之花"
        EQUIP_NECKLACE -> "死之羽"
        EQUIP_SHOES -> "时之沙"
        EQUIP_RING -> "空之杯"
        EQUIP_DRESS -> "理之冠"
        EQUIP_WEAPON -> "武器"
        else -> ""
    }


}