package com.lianyi.paimonsnotebook.common.database.cultivate.data


/*
* 计算物品类型
* */
enum class CultivateItemType {
    Skill,
    Avatar,
    Weapon,
    Reliquary,
    Overall //当使用此类型时,id总是负的cultivateItemId
}