package com.lianyi.paimonsnotebook.common.extension.color

import androidx.compose.ui.graphics.Color
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.ui.theme.Black

fun String.parseColor():Color{
    return try {
        Color(android.graphics.Color.parseColor(this))
    }catch (_:Exception){
        "错误的十六进制颜色码".errorNotify()
        Black
    }
}

fun Int.parseColor():Color{
    return try {
        Color(this)
    }catch (_:Exception){
        "错误的颜色值".errorNotify()
        Black
    }
}