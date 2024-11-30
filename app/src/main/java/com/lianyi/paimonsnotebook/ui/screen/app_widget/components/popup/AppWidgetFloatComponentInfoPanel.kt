package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.popup

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.lianyi.paimonsnotebook.common.components.placeholder.TextPlaceholder
import com.lianyi.paimonsnotebook.common.data.popup.FloatPositionProvider
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.Primary_3
import com.lianyi.paimonsnotebook.ui.theme.Transparent

@Composable
fun AppWidgetFloatComponentInfoPanel(
    popupProvider: FloatPositionProvider,
    onDrag: (Offset, Float) -> Unit
) {
    var expand by remember {
        mutableStateOf(true)
    }

    var isChangingSize by remember {
        mutableStateOf(false)
    }

    var scaleValue by remember {
        mutableFloatStateOf(1f)
    }

    val menuBorderColor by animateColorAsState(
        targetValue = if (isChangingSize) Primary_3 else Transparent,
        label = ""
    )

    Popup(
        properties = PopupProperties(),
        popupPositionProvider = popupProvider
    ) {
        Box(
            modifier = Modifier
                .scale(scaleValue)
                .radius(6.dp)
                .background(Black_30)
                .border(3.dp, menuBorderColor, RoundedCornerShape(6.dp))
                .padding(12.dp)
        ) {
            Column(
                modifier = Modifier.width(120.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .then(if (expand) Modifier.fillMaxWidth() else Modifier)
                        .pointerInput(Unit) {
                            detectDragGestures(onDrag = { _: PointerInputChange, offset: Offset ->
                                onDrag.invoke(offset, scaleValue)
                            })
                        }
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = { expand = !expand })
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Spacer(
                        modifier = Modifier
                            .clip(CircleShape)
                            .height(4.dp)
                            .background(Primary)
                            .width(if (expand) 32.dp else 16.dp)
                    )
                }

                Box(modifier = Modifier.size(120.dp)) {
                    TextPlaceholder()
                }
            }

            Spacer(modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(24.dp)
                .drawBehind {
                    drawArc(
                        Black,
                        5f,
                        80f,
                        false,
                        style = Stroke(width = 10f, cap = StrokeCap.Round)
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(onDragStart = {
                        println("size handle start = ${it}")
                        isChangingSize = true
                    }, onDrag = { _: PointerInputChange, offset: Offset ->

                        val x = if (offset.x != 0f) {
                            (size.width / (size.width - offset.x) - 1) * 0.1f
                        } else 0f

                        val y = if (offset.y != 0f) {
                            (size.height / (size.height - offset.y) - 1) * 0.1f
                        } else 0f

                        val sum = x + y * if(x > 0 && y > 0) .5f else 1f

                        scaleValue = (scaleValue + sum)
                            .coerceAtLeast(.5f)
                            .coerceAtMost(1.5f)

                        println("size handle drag = ${offset} scaleValue = ${scaleValue}")
                        println("x = ${x} y = ${y}")
                    }, onDragEnd = {
                        isChangingSize = false
                        println("onDragEnd")
                    })
                })
        }
    }
}