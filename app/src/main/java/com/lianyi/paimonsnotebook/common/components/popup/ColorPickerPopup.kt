package com.lianyi.paimonsnotebook.common.components.popup

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.godaddy.android.colorpicker.HsvColor
import com.godaddy.android.colorpicker.harmony.ColorHarmonyMode
import com.godaddy.android.colorpicker.harmony.HarmonyColorPicker
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.common.components.widget.TextSlider
import com.lianyi.paimonsnotebook.common.extension.color.parseColor
import com.lianyi.paimonsnotebook.common.extension.color.toHex
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black_60
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.Transparent
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_70

@Composable
fun ColorPickerPopup(
    visible: Boolean,
    onRequestDismiss: () -> Unit,
    onSelectColor: (Color) -> Unit,
    backgroundColor: Color = BackGroundColor,
) {
    var updatedHsvColor by remember {
        mutableStateOf(HsvColor.from(White))
    }

    BasePopup(visible = visible, onRequestDismiss = onRequestDismiss) {
        Box(modifier = Modifier.pointerInput(Unit) {}) {
            Icon(
                painter = painterResource(id = R.drawable.ic_dismiss),
                contentDescription = null,
                modifier = Modifier
                    .zIndex(1f)
                    .align(Alignment.TopEnd)
                    .radius(4.dp)
                    .padding(12.dp)
                    .size(32.dp)
                    .clickable {
                        onRequestDismiss.invoke()
                    }
            )

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
                HarmonyColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    harmonyMode = ColorHarmonyMode.NONE,
                    color = updatedHsvColor,
                    onColorChanged = { color: HsvColor ->
                        updatedHsvColor = color
                    },
                    showBrightnessBar = false
                )

                var text by remember {
                    mutableStateOf("")
                }

                remember(updatedHsvColor) {
                    updatedHsvColor.toColor().toHex().apply {
                        text = this
                    }
                }

                val focusManager = LocalFocusManager.current

                InputTextFiled(
                    value = text,
                    onValueChange = {
                        if (it.length < 10)
                            text = it
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(onDone = {
                        val color = if (text.startsWith("#")) {
                            text
                        } else {
                            "#${text}"
                        }.parseColor()

                        updatedHsvColor = HsvColor.from(color)
                        onSelectColor.invoke(color)
                        focusManager.clearFocus()
                    }),
                    backgroundColor = Transparent,
                    textStyle = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                )

                Column(
                    modifier = Modifier
                        .radius(8.dp)
                        .fillMaxWidth()
                        .background(BackGroundColor)
                        .padding(16.dp, 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextSlider(
                        value = updatedHsvColor.hue,
                        onValueChange = {
                            updatedHsvColor = updatedHsvColor.copy(hue = it)
                        },
                        text = {
                            "${it.toInt()}"
                        },
                        textContentBackgroundColor = Transparent,
                        range = (0f..360f),
                        title = "H"
                    )

                    TextSlider(value = updatedHsvColor.saturation, onValueChange = {
                        updatedHsvColor = updatedHsvColor.copy(saturation = it)
                    }, text = {
                        "${(it * 100).toInt()}"
                    }, textContentBackgroundColor = Transparent, title = "S")

                    TextSlider(value = updatedHsvColor.value, onValueChange = {
                        updatedHsvColor = updatedHsvColor.copy(value = it)
                    }, text = {
                        "${(it * 100).toInt()}"
                    }, textContentBackgroundColor = Transparent, title = "V")
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
                        onSelectColor.invoke(updatedHsvColor.toColor())
                    }
                }

                NavigationPaddingSpacer()
            }
        }

    }
}