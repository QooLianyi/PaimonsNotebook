package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.panel

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.extension.string.notify

@Composable
fun AppWidgetEditTransformItem(
    name: String,
    getValueByName: (String) -> Float,
    onValueChange: (String, Float) -> Unit,
    onDrag: (String, Float) -> Unit
) {
    val value = getValueByName.invoke(name)

    val realValue = if (value == -999f) {
        0f
    } else {
        value
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (value != -999f) {
                    Modifier.pointerInput(Unit) {
                        detectDragGesturesAfterLongPress(onDrag = { _: PointerInputChange, offset: Offset ->
                            val currentValue = getValueByName.invoke(name)
                            onValueChange.invoke(name, currentValue + (-offset.y / 10))
                        })
                    }
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        PrimaryText(
            text = name,
            modifier = Modifier.requiredWidthIn(14.dp, 100.dp),
            textAlign = TextAlign.Center
        )
        InputTextFiled(
            value = if (value == -999f) {
                "-"
            } else {
                if (realValue == realValue.toInt().toFloat())
                    realValue.toInt().toString() else "%.3f".format(realValue).trimEnd('0')
            },
            onValueChange = {
                onValueChange.invoke(name, it.toFloatOrNull() ?: 0f)
            },
            modifier = Modifier.weight(1f),
            inputFieldHeight = 24.dp,
            textStyle = TextStyle(fontSize = 16.sp),
            padding = PaddingValues(1.dp),
            borderWidth = 0.dp,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            contentAlignment = Alignment.CenterStart,
            keyboardActions = KeyboardActions {
                "keyboardActions".notify()
            }
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_up),
            contentDescription = null,
            modifier = Modifier
                .radius(2.dp)
                .size(24.dp)
                .clickable {
                    onValueChange.invoke(name, realValue + 1)
                }
        )

        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_down),
            contentDescription = null,
            modifier = Modifier
                .radius(2.dp)
                .size(24.dp)
                .clickable {
                    onValueChange.invoke(name, realValue - 1)
                }
        )
    }
}