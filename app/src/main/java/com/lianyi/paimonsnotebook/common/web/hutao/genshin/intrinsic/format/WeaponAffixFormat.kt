package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import kotlin.math.roundToInt

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
        setLevel(1f)
    }

    fun setLevel(level: Float) {
        currentLevel = if (level >= maxLevel) {
            maxLevel
        } else {
            level.roundToInt()
        }

        affixText = map[currentLevel - 1] ?: ""
    }
}
