package com.lianyi.paimonsnotebook.ui.screen.items.components.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.value.toPx
import kotlin.math.roundToInt

/*
* 物品界面顶部栏
* */
@Composable
internal fun BoxWithConstraintsScope.ItemScreenTopBar(
    text: String = "",
    lazyListState:LazyListState,
    added: Boolean = false,
    onClickListButton: () -> Unit,
    onClickAddButton: () -> Unit = {}
) {

    val bottomButtonHeightPx = remember {
        59.dp.toPx().roundToInt()
    }

    val maxHeightPx = remember {
        (this.maxHeight * .6f).toPx().roundToInt()
    }

    val offsetY by remember {
        derivedStateOf {
            when (lazyListState.firstVisibleItemIndex) {
                0 -> {
                    val offsetPx = maxHeightPx - bottomButtonHeightPx

                    val itemOffset = lazyListState.firstVisibleItemScrollOffset

                    if (itemOffset >= offsetPx) {
                        -(itemOffset - offsetPx)
                    } else {
                        0
                    }
                }

                else -> -maxHeightPx
            }
        }
    }

    Row(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .offset { IntOffset(0, offsetY) }
            .zIndex(2f),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        ItemActionButton(
            iconResId = R.drawable.ic_navigation,
            text = text
        ) {
            onClickListButton.invoke()
        }

        //todo 添加素材占位
        Spacer(modifier = Modifier.width(1.dp))

//        val addBackgroundColorAnim by animateColorAsState(
//            targetValue = if (added) Black else BlurCardBackgroundColor,
//            animationSpec = tween(100)
//        )
//        val addIconTintColorAnim by animateColorAsState(
//            targetValue = if (added) White else Black,
//            animationSpec = tween(100)
//        )
//
//        Row(
//            modifier = Modifier
//                .clip(RoundedCornerShape(4.dp))
//                .background(addBackgroundColorAnim)
//                .clickable {
//                    onClickAddButton.invoke()
//                }
//                .padding(8.dp)
//        ) {
//            Icon(
//                painter = painterResource(id = if (added) R.drawable.ic_checkmark_circle else R.drawable.ic_add_circle),
//                contentDescription = null,
//                modifier = Modifier.size(23.dp),
//                tint = addIconTintColorAnim
//            )
//        }
    }
}