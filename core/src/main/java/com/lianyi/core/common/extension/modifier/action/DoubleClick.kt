package com.lianyi.core.common.extension.modifier.action

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput

/*
* 双击拓展
* 使用双击需要两个方法,避免双击与单击事件冲突
*
*
* */
fun Modifier.doubleClick(clickBlock: (Offset) -> Unit, doubleClickBlock: (Offset) -> Unit) =
    pointerInput(Unit) {
        detectTapGestures(onTap = clickBlock, onDoubleTap = doubleClickBlock)
    }