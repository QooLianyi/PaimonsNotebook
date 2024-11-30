package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.popup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.lianyi.paimonsnotebook.common.data.popup.PopupWindowPositionProvider
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetComponentType
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun AppWidgetAddableComponentPopupWindow(
    popupProvider: PopupWindowPositionProvider,
    items: List<Pair<String, AppWidgetComponentType>>,
    onClickItem: (AppWidgetComponentType) -> Unit,
    onDismissRequest: () -> Unit
) {

    Popup(
        properties = PopupProperties(),
        onDismissRequest = onDismissRequest,
        popupPositionProvider = popupProvider
    ) {
        LazyColumn(
            modifier = Modifier
                .heightIn(50.dp, 1920.dp)
                .drawWithCache {
                    val indicatorWidth = popupProvider.indicatorWidthPx
                    val cornerRadius = 6.dp.toPx()

                    val path = Path().apply {
                        reset()

                        arcTo(
                            rect = Rect(
                                offset = Offset(0f, 0f),
                                size = Size(cornerRadius, cornerRadius),
                            ),
                            startAngleDegrees = -90f,
                            sweepAngleDegrees = -90f,
                            forceMoveTo = false
                        )

                        arcTo(
                            rect = Rect(
                                offset = Offset(
                                    0f,
                                    size.height - cornerRadius
                                ),
                                size = Size(cornerRadius, cornerRadius),
                            ),
                            startAngleDegrees = -180f,
                            sweepAngleDegrees = -90f,
                            forceMoveTo = false
                        )

                        arcTo(
                            rect = Rect(
                                offset = Offset(
                                    size.width - cornerRadius,
                                    size.height - cornerRadius
                                ),
                                size = Size(cornerRadius, cornerRadius),
                            ),
                            startAngleDegrees = 90f,
                            sweepAngleDegrees = -90f,
                            forceMoveTo = false
                        )

                        arcTo(
                            rect = Rect(
                                offset = Offset(
                                    size.width - cornerRadius,
                                    0f
                                ),
                                size = Size(cornerRadius, cornerRadius),
                            ),
                            startAngleDegrees = 0f,
                            sweepAngleDegrees = -90f,
                            forceMoveTo = false
                        )

                        close()
                    }

                    val indicatorPath = Path().apply {
                        reset()

                        moveTo(
                            popupProvider.indicatorHorizontalDrawStartPosition,
                            size.height
                        )
                        lineTo(
                            popupProvider.indicatorHorizontalDrawStartPosition - indicatorWidth / 4,
                            size.height
                        )
                        lineTo(
                            popupProvider.indicatorHorizontalDrawStartPosition,
                            size.height + indicatorWidth / 4
                        )
                        lineTo(
                            popupProvider.indicatorHorizontalDrawStartPosition + indicatorWidth / 4,
                            size.height
                        )

                        close()
                    }

                    onDrawBehind {
                        drawPath(
                            path = path,
                            color = Black
                        )

                        //如果弹窗超出顶部,并且有一边超出屏幕就将绘制的指示器路径旋转
                        rotate(if (popupProvider.overWindowTop || popupProvider.alignBottom) 180f else 0f) {
                            drawPath(
                                path = indicatorPath,
                                color = Black
                            )
                        }
                    }
                }
                .clickable {
                    onDismissRequest.invoke()
                }
                .padding(4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(items) {
                Text(
                    text = it.first, fontSize = 9.sp, color = White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .radius(1.dp)
                        .width(IntrinsicSize.Max)
                        .fillMaxWidth()
                        .clickable {
                            onClickItem.invoke(it.second)
                        }
                        .padding(4.dp, 2.dp)
                )
            }
        }
    }
}