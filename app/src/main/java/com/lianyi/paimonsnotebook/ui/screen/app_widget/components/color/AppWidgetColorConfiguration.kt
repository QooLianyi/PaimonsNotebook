package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.color

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.Info

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AppWidgetColorConfiguration(
    title: String,
    customColor: Color,
    colors: List<Color>,
    selectedIndex: Int,
    onSelected: (Color, Int) -> Unit,
) {
    Column {
        Text(
            text = title,
            fontSize = 14.sp,
            color = Info
        )
        Spacer(modifier = Modifier.height(4.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AppWidgetOptionColor(color = customColor, selected = selectedIndex == 0) {
                onSelected.invoke(customColor, 0)
            }
            colors.forEachIndexed { index, color ->
                AppWidgetOptionColor(color = color, selected = index + 1 == selectedIndex) {
                    onSelected.invoke(color, index + 1)
                }
            }
        }
    }
}