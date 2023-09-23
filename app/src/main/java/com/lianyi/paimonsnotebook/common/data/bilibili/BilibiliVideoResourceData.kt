package com.lianyi.paimonsnotebook.common.data.bilibili

data class BilibiliVideoResourceData(
    val code: Int,
    val `data`: Data,
    val message: String,
    val ttl: Int
) {
    data class Data(
        val accept_description: List<String>,
        val accept_format: String,
        val accept_quality: List<Int>,
        val durl: List<Durl>,
        val format: String,
        val from: String,
        val high_format: Any,
        val last_play_cid: Int,
        val last_play_time: Int,
        val message: String,
        val quality: Int,
        val result: String,
        val seek_param: String,
        val seek_type: String,
        val support_formats: List<SupportFormat>,
        val timelength: Int,
        val video_codecid: Int
    ) {
        data class Durl(
            val ahead: String,
            val backup_url: Any,
            val length: Int,
            val order: Int,
            val size: Int,
            val url: String,
            val vhead: String
        )

        data class SupportFormat(
            val codecs: Any,
            val display_desc: String,
            val format: String,
            val new_description: String,
            val quality: Int,
            val superscript: String
        )
    }
}