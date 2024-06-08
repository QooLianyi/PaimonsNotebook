package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData

data class WeaponAffixFormat(
    val affix: WeaponData.Affix?
) : BaseFormat() {

    private val map = affix?.Descriptions?.associate {
        it.Level to it.Description
    } ?: mapOf()

    var affixText by mutableStateOf("")
        private set

    var maxLevel: Int = affix?.Descriptions?.size ?: 1

    init {
        //初始化武器描述
        onSliderValueChange(1f)
    }

    //当滑动条值改变时
    fun onSliderValueChange(value: Float) {
        updateSliderValue(value)

        affixText = map[currentLevel - 1] ?: ""
    }
}
