package com.lianyi.paimonsnotebook.ui.screen.setting.data

import androidx.compose.runtime.Composable


data class OptionListData(
    val name: String,
    val description: String,
    val onClick: () -> Unit = {},
    val slot: @Composable () -> Unit = {},
)
