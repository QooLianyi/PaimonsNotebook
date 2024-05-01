package com.lianyi.paimonsnotebook.ui.screen.setting.components.widgets

import androidx.compose.runtime.Composable
import com.lianyi.paimonsnotebook.common.components.widget.Switch
import com.lianyi.paimonsnotebook.common.util.compose.provider.NoRippleThemeProvides

@Composable
fun SettingsOptionSwitch(
    checked: Boolean
) {
    NoRippleThemeProvides {
        Switch(checked = checked)
    }
}