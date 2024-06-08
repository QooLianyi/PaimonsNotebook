package com.lianyi.paimonsnotebook.common.data.popup

data class IconTitleInformationPopupWindowData(
    val title: String = "",
    val subTitle: String = "",
    val iconUrl: String = "",
    val content: String = "",
    val isMetadataIcon: Boolean = true,
    val maxLine: Int = 3
)
