package com.lianyi.paimonsnotebook.ui.screen.gacha.data

/*
* 兼容用,用于转化导入的uigf info字段,获取version信息
* */
data class UIGFInfoCompat(
    val uigfVersion: String?,
    val version: String?,
    val regionTimeZone: Long
)
