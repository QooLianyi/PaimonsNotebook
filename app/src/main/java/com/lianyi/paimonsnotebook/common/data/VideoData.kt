package com.lianyi.paimonsnotebook.common.data


/*
* width 视频宽度
* height 视频高度
* definition 清晰度
* label 标题
* url 视频地址
*
*
*
* */
data class VideoData(
    val width: Int = 1920,
    val height: Int = 1080,
    val definition: String = "",
    val label: String = "",
    val url:String,
    val isWeb:Boolean = false
)