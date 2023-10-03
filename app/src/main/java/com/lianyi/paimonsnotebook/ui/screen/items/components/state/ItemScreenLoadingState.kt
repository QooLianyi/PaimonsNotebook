package com.lianyi.paimonsnotebook.ui.screen.items.components.state

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor

@Composable
internal fun ItemScreenLoadingState(
    loadingState: LoadingState,
    content:@Composable ()->Unit
){
    Crossfade(targetState = loadingState, label = "") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackGroundColor)
        ) {
            when (it) {
                LoadingState.Error -> ErrorPlaceholder(text = "缺少所需的元数据")

                LoadingState.Success -> content.invoke()

                LoadingState.Loading ->
                    ContentLoadingPlaceholder(
                        text = "正在加载所需的数据",
                    )

                else -> {}
            }
        }
    }
}