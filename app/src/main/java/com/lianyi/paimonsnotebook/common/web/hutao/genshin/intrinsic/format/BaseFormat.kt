package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class BaseFormat {
    var currentLevel by mutableIntStateOf(1)
        private set

    var sliderValue by mutableFloatStateOf(1f)
        private set

    var show by mutableStateOf(false)

    fun toggleShow() {
        show = !show
    }

    fun updateSliderValue(value: Float) {
        sliderValue = value
        currentLevel = value.toInt()
    }
}