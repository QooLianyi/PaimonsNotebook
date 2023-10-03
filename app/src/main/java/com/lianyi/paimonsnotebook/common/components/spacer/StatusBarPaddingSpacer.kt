package com.lianyi.paimonsnotebook.common.components.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/*
* 状态栏内边距
* 防止内容显示在底部导航栏下方
* */
@Composable
fun StatusBarPaddingSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
    )
}