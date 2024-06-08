package com.lianyi.paimonsnotebook.common.components.layout.blur_card.widget

import androidx.compose.runtime.Composable
import com.lianyi.paimonsnotebook.common.components.widget.TextSlider

@Composable
fun ItemLevelSlider(
    value: Float,
    level: Int,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float> = (0f..90f),
) {
    TextSlider(value = value, onValueChange = onValueChange, range = range, text = {
        "Lv.${level}"
    })
}