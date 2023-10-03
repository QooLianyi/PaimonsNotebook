package com.lianyi.paimonsnotebook.common.util.compose.remember

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.util.window.WindowHelper

@Composable
fun rememberStatusBarHeightDp(): Dp {
    var calcStatusBarHeight = remember {
        true
    }
    var statusBarHeight = 0.dp
    if (calcStatusBarHeight) {
        calcStatusBarHeight = false
        statusBarHeight = WindowHelper.statusBarHeightDp
    }
    return statusBarHeight
}