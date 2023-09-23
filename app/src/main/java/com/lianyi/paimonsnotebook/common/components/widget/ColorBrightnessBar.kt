package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.godaddy.android.colorpicker.HsvColor
import com.lianyi.paimonsnotebook.common.extension.color.alpha
import com.lianyi.paimonsnotebook.common.extension.color.parseColor
import com.lianyi.paimonsnotebook.common.extension.color.toHex
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_40
import com.lianyi.paimonsnotebook.ui.theme.Transparent

@Composable
fun ColorBrightnessBar(
    onValueChange: (Float) -> Unit,
    currentColor: HsvColor,
    onSetHexColor: (HsvColor) -> Unit,
) {
    var text by remember {
        mutableStateOf("")
    }

    remember(currentColor) {
        currentColor.toColor().toHex().apply {
            text = this
        }
    }

    var value by remember {
        mutableStateOf(1f)
    }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        InputTextFiled(
            value = text,
            onValueChange = {
                if (it.length < 10)
                    text = it
            },
            modifier = Modifier.width(90.dp),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = {
                onSetHexColor.invoke(HsvColor.from(text.parseColor()))
            }),
            backgroundColor = Transparent
        )

        Spacer(modifier = Modifier.width(6.dp))

        val color = currentColor.toColor()

        TextSlider(value = value, onValueChange = {
            value = it
            onValueChange.invoke(it)
        }, text = {
            "${(it * 100).toInt()}"
        }, colors = SliderDefaults.colors(
            thumbColor = color,
            inactiveTrackColor = color.alpha(.4f),
            activeTrackColor = color.alpha(.4f),
        ), textMinWidth = 50.dp,
            textContentPadding = PaddingValues(8.dp, 4.dp)
        )
    }
}