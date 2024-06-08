package com.lianyi.paimonsnotebook.ui.screen.items.data.cultivate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateItemType
import kotlin.math.roundToInt

/*
* 养成计划配置数据类
* */
data class CultivateConfigData(
    val name: String,
    val iconUrl: String,
    val id: Int,
    val type: CultivateItemType,
    val itemTypeId:Int = -1, //物品类型id
    val fromLevel: Int = 1,
    val maxLevel: Int = 1,
    val tintIcon: Boolean = true
) {
    var toLevel by mutableIntStateOf(fromLevel)
        private set

    var sliderValue by mutableFloatStateOf(fromLevel.toFloat())

    var avatarEnergySkillId = -1

    fun setLevel(value: Float) {
        sliderValue = value
        this.toLevel = sliderValue.toInt()
    }
}
