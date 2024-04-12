package com.lianyi.paimonsnotebook.ui.screen.setting.components

import androidx.compose.runtime.Composable
import com.lianyi.paimonsnotebook.common.components.widget.IconTextHintSlotItem

@Composable
fun SettingOption(
    title: String,
    description: String,
    onClick: () -> Unit,
    slot: @Composable () -> Unit,
) {
    IconTextHintSlotItem(
        title = title,
        description = description,
        onClick = onClick,
        slot = slot
    )
}