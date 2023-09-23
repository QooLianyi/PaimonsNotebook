package com.lianyi.paimonsnotebook.ui.screen.setting.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.widget.IconTextHintSlotItem
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_70
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1

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
        backGroundColor = CardBackGroundColor_Light_1,
        onClick = onClick,
        slot = slot
    )
}