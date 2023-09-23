package com.lianyi.paimonsnotebook.ui.screen.items.components.widget

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.runtime.Composable
import com.lianyi.paimonsnotebook.common.components.blur_card.card.BlurCardTabButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ItemTabLayout(
    tabs:Array<String>,
    currentIndex:Int,
    onClick:(Int)->Unit
) {
    FlowRow {
        tabs.forEachIndexed { index, s ->
            BlurCardTabButton(
                text = s,
                index = index,
                currentIndex = currentIndex,
                onClick = onClick
            )
        }
    }
}