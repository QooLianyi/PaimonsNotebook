package com.lianyi.paimonsnotebook.common.extension.value

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/*
* 不跟随系统字体缩放的字体单位
* */
@Composable
fun Number.nonScaledSp(): TextUnit {
    return (this.toFloat() / LocalDensity.current.fontScale).sp
}

@Composable
fun Number.nonScaledSpPX(): Float {
    return (this.toFloat() / LocalDensity.current.fontScale).sp.toPx()
}