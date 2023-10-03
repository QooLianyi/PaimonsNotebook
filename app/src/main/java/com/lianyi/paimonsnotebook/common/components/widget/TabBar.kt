package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.lianyi.paimonsnotebook.ui.theme.Primary_2
import com.lianyi.paimonsnotebook.ui.theme.Transparent

@Composable
fun TabBar(
    modifier: Modifier = Modifier,
    tabs: Array<String>,
    indicatorColor: Color = Primary_2,
    requiredWidthIn: Pair<Dp, Dp> = 50.dp to 120.dp,
    textSelectColor: Color = Black,
    textUnSelectColor: Color = Black_20,
    textSelectSize: TextUnit = 20.sp,
    textUnSelectSize: TextUnit = 14.sp,
    onSelect: (Int) -> Unit,
) {

    var currentIndex by remember {
        mutableIntStateOf(0)
    }

    Row(
        modifier = modifier,
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

            val indicatorColorAnim by animateColorAsState(
                targetValue = if (currentIndex == index) indicatorColor else Transparent,
                label = ""
            )

            Column(
                modifier = Modifier
                    .requiredWidthIn(requiredWidthIn.first, requiredWidthIn.second)
                    .pointerInput(Unit){
                        detectTapGestures {
                            currentIndex = index
                            onSelect(currentIndex)
                        }
                    },
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = s, style = textStyle)

                if (indicatorColor != Transparent) {
                    Spacer(
                        modifier = Modifier
                            .height(3.dp)
                    )

                    Spacer(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(6.dp)
                            .background(indicatorColorAnim)
                    )
                }

            }
        }
    }
}