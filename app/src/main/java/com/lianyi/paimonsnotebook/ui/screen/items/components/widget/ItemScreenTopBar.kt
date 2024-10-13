package com.lianyi.paimonsnotebook.ui.screen.items.components.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.extension.value.toPx
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_60
import com.lianyi.paimonsnotebook.ui.theme.BlurCardBackgroundColor
import com.lianyi.paimonsnotebook.ui.theme.White
import kotlin.math.roundToInt

/*
* 物品界面顶部栏
* */
@Composable
internal fun BoxWithConstraintsScope.ItemScreenTopBar(
    text: String = "",
    lazyListState: LazyListState,
    added: Boolean = false,
    showAddButton:Boolean = true,
    onClickListButton: () -> Unit,
    onClickAddButton: () -> Unit = {}
) {

    val topButtonHeightPx = remember {
        //39(顶部按钮高度) + 10 * 2(顶部按钮外边距)
        59.dp.toPx().roundToInt()
    }

    val maxHeightPx = remember {
        (this.maxHeight * .6f).toPx().roundToInt()
    }

    val offsetY by remember {
        derivedStateOf {
            when (lazyListState.firstVisibleItemIndex) {
                0 -> {
                    val offsetPx = maxHeightPx - topButtonHeightPx

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

    Column {
        StatusBarPaddingSpacer()

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .offset { IntOffset(0, offsetY) },
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            ItemActionButton(
                iconResId = R.drawable.ic_navigation,
                text = text,
                onClick = onClickListButton
            )

            if(showAddButton){
                ItemActionButton(
                    iconResId = if (added) R.drawable.ic_checkmark_circle_full else R.drawable.ic_add_circle,
                    onClick = onClickAddButton,
                    iconTint = if (added) White else Black,
                    backgroundColor = if (added) Black_60 else BlurCardBackgroundColor
                )
            }
        }
    }
}