package com.lianyi.core.ui.components.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/*
* 内容底部导航栏内边距
* 防止内容显示在底部导航栏下方
* */
@Composable
fun NavigationBarPaddingSpacer() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    )
}