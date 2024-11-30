package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.app_widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.placeholder.TextPlaceholder
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetComponentType
import com.lianyi.paimonsnotebook.common.extension.modifier.offset.rotateBy
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.AppWidgetEditData
import com.lianyi.paimonsnotebook.ui.theme.Primary_3
import com.lianyi.paimonsnotebook.ui.theme.Warning

@Composable
fun AppWidgetComponent(
    component: AppWidgetEditData.Component,
    onClick: (AppWidgetEditData.Component) -> Unit,
    onDrag: (Offset) -> Unit
) {
    val base = component.baseComponent

    key(component.uuid) {
        Box(
            modifier = Modifier
                .graphicsLayer {
                    rotationZ = base.rotate
                    translationX = base.x
                    translationY = base.y
                }
                .size(base.width.dp, base.height.dp)
                .then(
                    if (component.isSelected) Modifier
                        .pointerInput(Unit) {
                            detectDragGestures { _, dragAmount ->
                                onDrag.invoke(dragAmount.rotateBy(base.rotate))
                            }
                        }
                        .border(1.dp, Primary_3)
                    else Modifier
                )
                .then(
                    if (component.showBorder) Modifier
                        .border(1.dp, Warning)
                    else Modifier
                )
                .pointerInput(Unit) {
                    detectTapGestures {
                        onClick.invoke(component)
                    }
                }
        ) {

            when (component.type) {
                AppWidgetComponentType.Text -> {
                    val text = component.text ?: return

                    Text(
                        text = text.stringValueOrigin,
                        modifier = Modifier.fillMaxSize(),
                        letterSpacing = text.textSpacer.sp,
                        style = TextStyle(
                            color = Color(text.color),
                            fontSize = text.textSize.sp,
                            fontWeight = if (text.isBold) FontWeight.SemiBold else FontWeight.Normal,
                            fontStyle = if (text.isItalic) FontStyle.Italic else FontStyle.Normal,
                            textDecoration = TextDecoration.combine(
                                listOf(
                                    if (text.isUnderLine) TextDecoration.Underline else TextDecoration.None,
                                    if (text.isStrikeThrough) TextDecoration.LineThrough else TextDecoration.None,
                                )
                            )
                        )
                    )
                }

                AppWidgetComponentType.Image -> {
                    val image = component.image ?: return

                    val data = image.list.first().value

                    if (image.isUrl) {
                        NetworkImage(
                            url = data,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    if (image.isResId) {
                        Image(
                            painter = painterResource(id = data.toInt()),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                AppWidgetComponentType.Line -> {
                    val line = component.line ?: return

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(line.color))
                    )
                }

                AppWidgetComponentType.Rectangle -> {
                    val rect = component.rectangle ?: return

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(rect.color))
                    )
                }

                AppWidgetComponentType.Triangle -> {
                    val triangle = component.triangle ?: return

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                val path = Path().apply {
                                    reset()
                                    moveTo(size.width / 2, 0f)
                                    lineTo(0f, size.height)
                                    lineTo(size.width, size.height)
                                    lineTo(size.width / 2, 0f)
                                    close()
                                }

                                drawPath(
                                    path = path,
                                    color = Color(triangle.color),
                                    style = if (triangle.isFull) Fill else Stroke(width = triangle.lineWidth)
                                )
                            }
                    )
                }

                AppWidgetComponentType.Circle -> {
                    val circle = component.circle ?: return

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .drawBehind {
                                drawCircle(
                                    color = Color(circle.color),
                                    radius = size.height / 2,
                                    style = if (circle.isFull) Fill else Stroke(width = circle.lineWidth)
                                )
                            }
                    )
                }


                else -> {
                    TextPlaceholder("未支持的类型:${component.type}")
                }
            }
        }
    }
}