package com.lianyi.paimonsnotebook.ui.screen.items.components.state

import androidx.compose.runtime.Composable
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingLayout
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState

@Composable
internal fun ItemScreenLoadingState(
    loadingState: LoadingState,
    content: @Composable () -> Unit
) {
    ContentLoadingLayout(
        loadingState = loadingState,
        loadingContent = {
            ContentLoadingPlaceholder(
                text = "正在加载所需的数据",
            )
        },
        successContent = content,
        emptyContent = {},
        errorContent = {
            ErrorPlaceholder(text = "缺少所需的元数据")
        },
        defaultContent = {}
    )
}