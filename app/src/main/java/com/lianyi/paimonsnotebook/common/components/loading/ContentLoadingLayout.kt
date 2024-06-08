package com.lianyi.paimonsnotebook.common.components.loading

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor


/*
* 内容加载布局,根据不同的加载状态显示不同的内容
* */
@Composable
fun ContentLoadingLayout(
    loadingState: LoadingState,
    loadingContent: @Composable () -> Unit = {},
    emptyContent: @Composable () -> Unit = {},
    errorContent: @Composable () -> Unit = {},
    defaultContent: @Composable () -> Unit = {},
    noDataContent: @Composable () -> Unit = {},
    successContent: @Composable () -> Unit = {}
) {
    Crossfade(
        targetState = loadingState, label = "",
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor)
    ) {

        when (it) {
            LoadingState.Loading -> {
                loadingContent.invoke()
            }

            LoadingState.Success -> {
                successContent.invoke()
            }

            LoadingState.Empty -> {
                emptyContent.invoke()
            }

            LoadingState.Error -> {
                errorContent.invoke()
            }

            LoadingState.NoData -> {
                noDataContent.invoke()
            }

            else -> {
                defaultContent.invoke()
            }
        }
    }
}