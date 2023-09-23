package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post

/*
* 结构对象视频
* */
data class StructuredVod(
    val cover: String,
    val duration: Int,
    val id: String,
    val resolutions: List<Resolution>,
    val review_status: Int,
    val transcoding_status: Int,
    val view_num: Int
){
    data class Resolution(
        val bitrate: Int,
        val definition: String,
        val format: String,
        val height: Int,
        val label: String,
        val size: Int,
        val url: String,
        val width: Int
    )
}

