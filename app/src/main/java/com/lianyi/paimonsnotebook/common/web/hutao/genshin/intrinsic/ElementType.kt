package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic

import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.theme.ElementElectricColor
import com.lianyi.paimonsnotebook.ui.theme.ElementFireColor
import com.lianyi.paimonsnotebook.ui.theme.ElementGrassColor
import com.lianyi.paimonsnotebook.ui.theme.ElementIceColor
import com.lianyi.paimonsnotebook.ui.theme.ElementRockColor
import com.lianyi.paimonsnotebook.ui.theme.ElementWaterColor
import com.lianyi.paimonsnotebook.ui.theme.ElementWindColor
import com.lianyi.paimonsnotebook.ui.theme.Primary

/*
* 元素类型
* */
object ElementType {

    //通过名称获取id
    fun getElementTypeByName(name: String) =
        when (name) {
            "火" -> Fire
            "水" -> Water
            "草" -> Grass
            "雷" -> Electric
            "冰" -> Ice
            "风" -> Wind
            "岩" -> Rock
            else -> None
        }

    fun getElementNameByType(type: Int) =
        when (type) {
            Fire -> "火"
            Water -> "水"
            Grass -> "草"
            Electric -> "雷"
            Ice -> "冰"
            Wind -> "风"
            Rock -> "岩"
            else -> ""
        }


    //通过元素名称获取对应的资源id
    fun getElementResourceIdByName(name: String) =
        when (getElementTypeByName(name)) {
            Fire -> R.drawable.ic_genshin_game_element_fire
            Water -> R.drawable.ic_genshin_game_element_water
            Grass -> R.drawable.ic_genshin_game_element_grass
            Electric -> R.drawable.ic_genshin_game_element_electric
            Ice -> R.drawable.ic_genshin_game_element_ice
            Wind -> R.drawable.ic_genshin_game_element_wind
            Rock -> R.drawable.ic_genshin_game_element_rock
            else -> R.drawable.icon_area_summer_land
        }

    fun getElementResourceIdByType(type: Int) =
        getElementResourceIdByName(getElementNameByType(type))

    fun getElementColorByName(name: String) =
        when (name.lowercase()) {
            "火","pyro" -> ElementFireColor
            "水","hydro" -> ElementWaterColor
            "草","dendro" -> ElementGrassColor
            "雷","electro" -> ElementElectricColor
            "冰","cryo" -> ElementIceColor
            "风","anemo" -> ElementWindColor
            "岩","geo" -> ElementRockColor
            else -> Primary
        }

    const val None = 0 // 无元素
    const val Fire = 1 // 火元素
    const val Water = 2 // 水元素
    const val Grass = 3 // 草元素
    const val Electric = 4 // 雷元素
    const val Ice = 5 // 冰元素
    const val Frozen = 6 // 冻元素
    const val Wind = 7 // 风元素
    const val Rock = 8 // 岩元素
    const val AntiFire = 9 // 燃元素
    const val VehicleMuteIce = 10 // 枫丹玩法
    const val Mushroom = 11 // 弹弹菇
    const val Overdose = 12 // 激元素
    const val Wood = 13 // 木元素
    const val Count = 14 // 个数
}