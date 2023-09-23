package com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

open class BaseFormat {
    var currentLevel by mutableIntStateOf(1)

    var show by mutableStateOf(false)

    fun toggleShow() {
        show = !show
    }
}