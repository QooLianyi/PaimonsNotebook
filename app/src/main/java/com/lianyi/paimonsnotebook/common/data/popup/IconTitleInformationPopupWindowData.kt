package com.lianyi.paimonsnotebook.common.data.popup

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class IconTitleInformationPopupWindowData(
    val title: String = "",
    val subTitle: String = "",
    val iconUrl: String = "",
    val content: String = "",
    val isMetadataIcon: Boolean = true,
    val maxLine: Int = 3,
    val width:Dp = 180.dp
)
