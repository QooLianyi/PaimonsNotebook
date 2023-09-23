package com.lianyi.paimonsnotebook.common.components.blur_card.widget

import androidx.compose.runtime.Composable
import com.lianyi.paimonsnotebook.common.components.widget.TextSlider

@Composable
fun ItemSlider(
    value: Int,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float> = (0f..90f),
) {
    TextSlider(value = value.toFloat(), onValueChange = onValueChange, range = range, text = {
        "Lv.${value}"
    })
}