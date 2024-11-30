package com.lianyi.paimonsnotebook.common.data.popup

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider
import com.lianyi.paimonsnotebook.common.extension.value.toPx
import kotlin.math.roundToInt

class PopupWindowPositionProvider(
    private val contentOffset: Offset = Offset.Zero,
    private val itemSize: IntSize = IntSize.Zero,
    private val itemSpace: Dp = Dp(0f),
    private val indicatorWidth: Dp = Dp(10f),
    val alignBottom:Boolean = false,
    private val contentPaddingValue: PaddingValues = PaddingValues(Dp(0f)),
) : PopupPositionProvider {

    //弹窗内部的边距
    private val popupWindowInnerContentPaddingPx = Dp(8f).toPx()

    //指示器像素宽度
    val indicatorWidthPx = indicatorWidth.toPx()

    //指示器水平绘制开始点
    var indicatorHorizontalDrawStartPosition = 0f
        private set

    //是否超出顶部窗口
    var overWindowTop = false
        private set

    var overWindowStartSide = false
        private set

    var overWindowEndSide = false
        private set

    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {

        //为指示器水平绘制开始点设置默认值(item的水平中点)
        indicatorHorizontalDrawStartPosition =
            popupContentSize.width / 2 - indicatorWidthPx / 2 +
                    popupWindowInnerContentPaddingPx / 2 + Dp(1f).toPx() //添加1dp的修正

        //item组件像素尺寸
        val itemWidthPx = itemSize.width
        val itemWidthPxHalf = itemWidthPx / 2

        val itemHeightPx = itemSize.height

        //弹窗尺寸
        val popupContentWidthPxHalf = popupContentSize.width / 2


        //组件边距(垂直)
        val itemSpacePx = itemSpace.toPx().roundToInt()

        //内容边距像素
        val contentPaddingStartPx = contentPaddingValue.calculateStartPadding(layoutDirection)
            .toPx()
        //内容边距像素
        val contentPaddingEndPx = contentPaddingValue.calculateEndPadding(layoutDirection)
            .toPx()

        //窗口的高度
        val windowWidth = windowSize.width

        //弹窗x偏移位置
        val popupWindowOffsetX = popupContentWidthPxHalf - itemWidthPxHalf

        //实际上的偏移位置
        //偏移位置=当前组件位置+左边距位置 * 1.25f - 弹窗所需宽度
        val x = contentOffset.x + contentPaddingStartPx - popupWindowOffsetX

        //判断弹窗是否超过屏幕的开始位置(左边)
        overWindowStartSide = contentOffset.x - popupWindowOffsetX < 0

        //判断弹窗是否超过屏幕结束的位置(右边)
        overWindowEndSide =
            contentOffset.x + popupContentWidthPxHalf + itemWidthPxHalf > windowWidth


        //弹窗所需高度
        val popupWindowRequiredHeight = popupContentSize.height + itemSpacePx

        //判断是否超出窗口顶部
        overWindowTop = contentOffset.y < popupWindowRequiredHeight
        //判断是否超出窗口两边

        //当超出窗口顶部时,将窗口开始位置移动至item的底部
        val y = if (overWindowTop || alignBottom) {
            contentOffset.y + itemSpacePx + itemHeightPx
        } else {
            contentOffset.y - popupContentSize.height - itemSpace.toPx().roundToInt()
        }

        //设置指示器的水平起始位置
        if (overWindowStartSide && !overWindowTop) {
            indicatorHorizontalDrawStartPosition =
                contentOffset.x + itemWidthPxHalf
        }

        if (overWindowEndSide && overWindowEndSide) {
            indicatorHorizontalDrawStartPosition =
                windowWidth - contentOffset.x - itemWidthPxHalf
        }

        if (overWindowEndSide && !overWindowTop) {
            //页面end的边距
            val pageEndPadding = windowWidth - contentOffset.x - itemWidthPx

            indicatorHorizontalDrawStartPosition =
                popupContentSize.width - itemWidthPxHalf - pageEndPadding
        }

        if (overWindowStartSide && overWindowTop) {
            indicatorHorizontalDrawStartPosition =
                popupContentSize.width - itemWidthPxHalf - contentOffset.x
        }

        return IntOffset(x.toInt(), y.toInt())
    }
}