package com.lianyi.paimonsnotebook.common.components.media

import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.rememberPagerState
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingBottom
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

@OptIn(ExperimentalPagerApi::class)
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

    //开始索引
    val startIndex by remember {
        mutableStateOf((Int.MAX_VALUE / 2) - (Int.MAX_VALUE / 2) % bannerData.size)
    }
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(startIndex)

    val isDragged by pagerState.interactionSource.collectIsFocusedAsState()

    //启用自动轮播
    if (enableAutoLoop && !isDragged) {
        LaunchedEffect(Unit) {
            while (true) {
                yield()
                delay(delay)
                val target = pagerState.currentPage + 1
                scope.launch {
                    val realTarget = when (target) {
                        Int.MAX_VALUE -> {
                            pagerState.scrollToPage(0)
                            0
                        }
                        1 -> {
                            pagerState.scrollToPage(startIndex)
                            startIndex
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
            count = Int.MAX_VALUE,
            modifier = modifier,
            state = pagerState,
            contentPadding = contentPaddingValues,
            itemSpacing = itemSpacing
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
            indicator(bannerData.size , pagerState.currentPage % bannerData.size)
        }
    }
}


/*
* 实现默认指示器的banner
*
* */
@OptIn(ExperimentalPagerApi::class)
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
//            Indicator(currentPageIndex,index, modifier = Modifier.paddingBottom(8.dp))
        }
    )

}
