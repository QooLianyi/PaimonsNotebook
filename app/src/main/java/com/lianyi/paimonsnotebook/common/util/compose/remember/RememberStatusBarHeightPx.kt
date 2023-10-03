package com.lianyi.paimonsnotebook.common.util.compose.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.lianyi.paimonsnotebook.common.util.window.WindowHelper

@Composable
fun rememberStatusBarHeightPx(): Float {
    var calcStatusBarHeight = remember {
        true
    }
    var statusBarHeight = 0f
    if (calcStatusBarHeight) {
        calcStatusBarHeight = false
        statusBarHeight = WindowHelper.statusBarHeightPx
    }
    return statusBarHeight
}