package com.lianyi.paimonsnotebook.common.components.media

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

/*
* bannerData 轮播图数据
* modifier
* delay自动轮播时间，不启用轮播无效
* enableAutoLoop 是否启用自动轮播
* contentPaddingValues 内容之间的间距
* itemSpacing 页面之间的间距，默认为0 可为负值
* indicatorAlign 指示器对齐方位
* indicatorOffset 指示器偏移量
* indicator 指示器插槽
* content 轮播图item
*
* 使用Pager库，将pager当前索引设置为一个极大的值来达到伪无限轮播的效果
*
* */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> LazyBanner(
    bannerData: List<T>,
    modifier: Modifier = Modifier,
    delay: Long = 3000,
    enableAutoLoop: Boolean = true,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = Dp(0f),
    indicatorAlign: Alignment = Alignment.BottomCenter,
    indicatorOffset: IntOffset = IntOffset.Zero,
    indicator: @Composable (count: Int, currentPageIndex: Int) -> Unit,
    content: @Composable PagerScope.(data: T, itemIndex: Int, currentIndex: Int) -> Unit,
) {
    //空列表直接返回
    if (bannerData.isEmpty()) return

    val pageCount = bannerData.size * 100

    //开始索引
    val startIndex by remember {
        mutableIntStateOf((pageCount / 2) - (pageCount / 2) % bannerData.size)
    }
    val pagerState = rememberPagerState(initialPage = startIndex) {
        pageCount
    }

    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()

    //启用自动轮播
    if (enableAutoLoop && !isDragged) {
        LaunchedEffect(Unit) {
            while (true) {
                yield()
                delay(delay)
                val target = pagerState.currentPage + 1

                launch {
                    val realTarget = when (target) {
                        pageCount, pageCount - bannerData.size -> {
                            pagerState.scrollToPage(startIndex)
                            startIndex
                        }

                        1 -> {
                            pagerState.scrollToPage(startIndex)
                            startIndex + 1
                        }

                        else -> pagerState.currentPage + 1
                    }
                    pagerState.animateScrollToPage(realTarget)
                }
            }
        }
    }

    Box {
        HorizontalPager(
            modifier = modifier,
            state = pagerState,
            contentPadding = contentPaddingValues,
            pageSpacing = itemSpacing
        ) { currentIndex ->

            val index = currentIndex % bannerData.size
            val pageCurrent = pagerState.currentPage % bannerData.size

            val item = bannerData[index]
            content(this, item, index, pageCurrent)
        }

        Row(modifier = Modifier
            .align(indicatorAlign)
            .offset {
                indicatorOffset
            }) {
            indicator(bannerData.size, pagerState.currentPage % bannerData.size)
        }
    }
}


/*
* 实现默认指示器的banner
*
* */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> LazyBanner(
    bannerData: List<T>,
    modifier: Modifier = Modifier,
    delay: Long = 3000,
    enableAutoLoop: Boolean = true,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = Dp(0f),
    content: @Composable PagerScope.(data: T, itemIndex: Int, currentIndex: Int) -> Unit,
) {

    LazyBanner(
        bannerData = bannerData,
        modifier = modifier,
        delay = delay,
        enableAutoLoop = enableAutoLoop,
        contentPaddingValues = contentPaddingValues,
        itemSpacing = itemSpacing,
        content = content,
        indicator = { index, currentPageIndex ->
            Indicator(currentPageIndex, index, modifier = Modifier.padding(bottom = 4.dp))
        }
    )

}
