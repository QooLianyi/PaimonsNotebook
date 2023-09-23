package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs

data class ActivityCalendarData(
    val list: List<ActivityCalendarDataItem>
) {
    data class ActivityCalendarDataItem(
        val break_type: String,
        val contentInfos: List<ContentInfo>,
        val contentSource: List<ContentSource>,
        val content_id: String,
        val drop_day: List<String>,
        val end_time: String,
        val font_color: String,
        val id: String,
        val img_url: String,
        val jump_type: String,
        val jump_url: String,
        val kind: String,
        val padding_color: String,
        val sort: String,
        val start_time: String,
        val style: String,
        val title: String
    ) {
        data class ContentInfo(
            val bbs_url: String,
            val content_id: Int,
            val icon: String,
            val title: String
        )

        data class ContentSource(
            val bbs_url: String,
            val content_id: Int,
            val icon: String,
            val title: String
        )
    }
}