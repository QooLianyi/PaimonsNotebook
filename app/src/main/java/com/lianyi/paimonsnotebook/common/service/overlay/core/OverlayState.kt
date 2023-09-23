package com.lianyi.paimonsnotebook.common.service.overlay.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.IntOffset

/*
* 悬浮窗状态
* offset:控制器初始的偏移位置
* */
class OverlayState(offset: IntOffset = IntOffset.Zero) {
    //是否显示内容
    var showContent by mutableStateOf(false)

    //控制组件偏移量
    var controllerOffset by mutableStateOf(offset)
    var showController by mutableStateOf(true)

    //内容的最大宽高
    var overlayWidthPx = Int.MAX_VALUE
    var overlayHeightPx = Int.MAX_VALUE
}