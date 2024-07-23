package com.lianyi.paimonsnotebook.common.components.popup

import android.graphics.PointF
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.github.skydoves.colorpicker.compose.AlphaSlider
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black_60
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.White_70

@Composable
fun ColorPickerPopup(
    visible: Boolean,
    initialPointer: PointF? = null,
    initialColor: Color? = null,
    onRequestDismiss: () -> Unit,
    onSelectColor: (Color, PointF) -> Unit,
    backgroundColor: Color = BackGroundColor,
) {
    val controller = rememberColorPickerController().apply {
        setDebounceDuration(100)
    }

    BasePopup(visible = visible, onRequestDismiss = onRequestDismiss) {

        Box(modifier = Modifier.pointerInput(Unit) {}) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .zIndex(1f),
                horizontalAlignment = Alignment.End
            ) {
                StatusBarPaddingSpacer()

                Icon(
                    painter = painterResource(id = R.drawable.ic_dismiss),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(14.dp)
                        .radius(4.dp)
                        .size(32.dp)
                        .clickable {
                            onRequestDismiss.invoke()
                        }
                )

            }

            Column(
                modifier = Modifier
                    .zIndex(0f)
                    .fillMaxSize()
                    .background(backgroundColor)
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                StatusBarPaddingSpacer()

                var text by remember {
                    mutableStateOf("")
                }

                Box {
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        controller = controller,
                        initialColor = initialColor,
                        onColorChanged = {
                            text = it.hexCode
                        }
                    )
                }

                Column(
                    modifier = Modifier
                        .radius(8.dp)
                        .fillMaxWidth()
                        .background(BackGroundColor)
                        .padding(16.dp, 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        com.lianyi.core.ui.components.text.PrimaryText(
                            text = "透明度",
                            modifier = Modifier.width(60.dp)
                        )

                        AlphaSlider(
                            controller = controller,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        com.lianyi.core.ui.components.text.PrimaryText(
                            text = "亮度",
                            modifier = Modifier.width(60.dp)
                        )

                        BrightnessSlider(
                            controller = controller,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AlphaTile(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .radius(8.dp)
                                .size(60.dp),
                            controller
                        )

                        com.lianyi.core.ui.components.text.PrimaryText(
                            text = text,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    TextButton(
                        text = "取消",
                        backgroundColor = White_70,
                        modifier = Modifier.weight(1f),
                        textColor = Black_60,
                        radius = 6.dp
                    ) {
                        onRequestDismiss.invoke()
                    }

                    TextButton(
                        text = "确定",
                        backgroundColor = Primary,
                        modifier = Modifier.weight(1f),
                        radius = 6.dp
                    ) {
                        onSelectColor.invoke(
                            controller.selectedColor.value,
                            controller.selectedPoint.value
                        )
                    }
                }

                NavigationBarPaddingSpacer()
            }
        }

    }
}