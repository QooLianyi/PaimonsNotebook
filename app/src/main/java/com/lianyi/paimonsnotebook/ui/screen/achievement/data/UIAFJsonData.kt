package com.lianyi.paimonsnotebook.ui.screen.achievement.data

/*
* UIAFJson
* */
data class UIAFJsonData(
    val info: Info,
    val list: List<Item>
) {
    data class Info(
        val export_app: String,
        val export_app_version: String,
        val export_timestamp: Long,
        val uiaf_version: String
    )

    data class Item(
        val id: Int,
        val timestamp: Int,
        val current: Int,
        val status: Int
    )
}

