package com.lianyi.paimonsnotebook.common.components.popup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.components.text.InfoText
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.data.popup.IconTitleInformationPopupWindowData
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 材料信息弹窗
* */
@Composable
fun IconTitleInformationPopupWindow(
    data: IconTitleInformationPopupWindowData,
    popupProvider: InformationPopupPositionProvider,
    onDismissRequest: () -> Unit
) {
    Popup(
        properties = PopupProperties(),
        onDismissRequest = onDismissRequest,
        popupPositionProvider = popupProvider
    ) {

        Box {

            Column(modifier = Modifier
                .width(180.dp)
                .drawWithCache {
                    val indicatorWidth = popupProvider.indicatorWidthPx
                    val cornerRadius = 14.dp.toPx()

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
                            popupProvider.indicatorHorizontalDrawStartPosition - indicatorWidth / 2,
                            size.height
                        )
                        lineTo(
                            popupProvider.indicatorHorizontalDrawStartPosition,
                            size.height + indicatorWidth / 2
                        )
                        lineTo(
                            popupProvider.indicatorHorizontalDrawStartPosition + indicatorWidth / 2,
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
                        rotate(if (popupProvider.overWindowTop) 180f else 0f) {
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
                .padding(8.dp)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    NetworkImageForMetadata(
                        url = data.iconUrl,
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Column {
                        PrimaryText(
                            text = data.title,
                            fontSize = 14.sp,
                            color = White
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        InfoText(text = data.subTitle, fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                InfoText(
                    text = data.content,
                    maxLines = data.maxLine
                )
            }
        }
    }
}