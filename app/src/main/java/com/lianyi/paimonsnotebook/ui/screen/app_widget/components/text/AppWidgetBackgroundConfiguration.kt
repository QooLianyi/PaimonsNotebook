package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.text

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.Info

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AppWidgetBackgroundConfiguration(
    backgroundOptions: List<Pair<String, String>>,
    currentBackgroundOption: String,
    onClickOption: (Pair<String, String>) -> Unit,
) {
    Column {
        Text(
            text = "背景主题",
            fontSize = 14.sp,
            color = Info
        )
        Spacer(modifier = Modifier.height(4.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            backgroundOptions.forEach { option ->
                AppWidgetOptionText(text = option.first, currentBackgroundOption == option.second) {
                    onClickOption.invoke(option)
                }
            }
        }
//
//        Spacer(modifier = Modifier.height(6.dp))
//
//        Text(
//            text = "*自定义背景颜色只有特定的组件能够设置",
//            fontSize = 10.sp,
//            color = Info
//        )
    }
}