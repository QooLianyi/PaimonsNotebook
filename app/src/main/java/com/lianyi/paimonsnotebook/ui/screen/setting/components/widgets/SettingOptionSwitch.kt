package com.lianyi.paimonsnotebook.ui.screen.setting.components.widgets

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.widget.Switch
import com.lianyi.paimonsnotebook.common.util.compose.provider.NoRippleThemeProvides
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.Primary_2
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun SettingsOptionSwitch(
    checked:Boolean
) {
    NoRippleThemeProvides{
        Switch(
            checked = checked,
            checkedThumbColor = White,
            checkedTrackColor = Primary_2,
            uncheckedThumbColor = Primary,
            uncheckedTrackColor = White,
            barWidth = 38.dp,
            barHeight = 16.dp
        )
    }
}