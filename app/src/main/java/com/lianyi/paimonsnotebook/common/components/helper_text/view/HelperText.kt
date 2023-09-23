package com.lianyi.paimonsnotebook.common.components.helper_text.view

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import com.lianyi.paimonsnotebook.common.util.enums.HelperTextStatus
import com.lianyi.paimonsnotebook.ui.theme.*

/*
* 辅助说明/额外说明/帮助文字
* 用于输入框下对输入框内的内容进行额外说明
*
* */

@Composable
fun HelperText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit = 12.sp,
    spacerWidth: Dp = 10.dp,
    availableColor: Color = colorPrimary,
    disableColor: Color = Info_1,
    errorColor: Color = Error,
    warningColor: Color = Warning,
    status: HelperTextStatus = HelperTextStatus.Disable,
) {
    val offsetAnim = remember {
        Animatable(IntOffset.Zero, IntOffset.VectorConverter)
    }

    val indicatorColor = when (status) {
        HelperTextStatus.Available -> availableColor
        HelperTextStatus.Warning -> warningColor
        HelperTextStatus.Error -> errorColor
        else -> disableColor
    }

    val textColor = when (status) {
        HelperTextStatus.Warning -> warningColor
        HelperTextStatus.Error -> errorColor
        HelperTextStatus.Available -> Black
        else -> Info
    }

    val indicatorColorAnim by animateColorAsState(targetValue = indicatorColor)
    val textColorAnim by animateColorAsState(targetValue = textColor)

    LaunchedEffect(status) {
        if (status == HelperTextStatus.Error) {
            offsetAnim.animateTo(
                IntOffset.Zero,
                spring(.15f),
                IntOffset(-200, 200)
            )
        }
    }

    Row(modifier = modifier) {
        Row(modifier = Modifier.offset {
            offsetAnim.value
        }, verticalAlignment = Alignment.CenterVertically) {
            Canvas(modifier = Modifier.size(10.dp), onDraw = {
                drawCircle(color = indicatorColorAnim)
            })

            Spacer(modifier = Modifier.width(spacerWidth))

            Text(text = text, fontSize = fontSize, color = textColorAnim)
        }
    }
}

