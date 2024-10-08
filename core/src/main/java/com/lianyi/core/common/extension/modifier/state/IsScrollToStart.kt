package com.lianyi.core.common.extension.modifier.state

import androidx.compose.foundation.lazy.LazyListState


//列表是否滚动到底部(显示的是否是最后一个item)

val LazyListState.isScrollToStart: Boolean
    get() = this.layoutInfo.visibleItemsInfo.firstOrNull()?.index == 0