package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.util.compose.animate.animateTextStyleAsState
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_20

@Composable
fun TabBar(
    tabs: Array<String>,
    tabBarPadding: PaddingValues,
    index: Int = 0,
    requiredWidthIn: Pair<Dp, Dp> = 50.dp to 240.dp,
    textSelectColor: Color = Black,
    textUnSelectColor: Color = Black_20,
    textSelectSize: TextUnit = 20.sp,
    textUnSelectSize: TextUnit = 14.sp,
    tabsSpace: Dp = 0.dp,
    onSelect: (Int) -> Unit
) {

    var currentIndex by remember(index) {
        mutableIntStateOf(index)
    }

    Row(
        modifier = Modifier.padding(tabBarPadding)
    ) {

        //以选中的字体样式撑起高度
        Text(
            text = "", style = TextStyle(
                color = textSelectColor,
                fontSize = textSelectSize,
                fontWeight = FontWeight.SemiBold
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(tabsSpace),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEachIndexed { index, s ->

                val textStyle by animateTextStyleAsState(
                    targetValue = if (currentIndex == index) TextStyle(
                        color = textSelectColor,
                        fontSize = textSelectSize,
                        fontWeight = FontWeight.SemiBold
                    )
                    else TextStyle(
                        color = textUnSelectColor,
                        fontSize = textUnSelectSize,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Column(
                    modifier = Modifier
                        .requiredWidthIn(requiredWidthIn.first, requiredWidthIn.second)
                        .pointerInput(Unit) {
                            detectTapGestures {
                                currentIndex = index
                                onSelect.invoke(currentIndex)
                            }
                        },
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = s, style = textStyle)
                }
            }
        }
    }
}