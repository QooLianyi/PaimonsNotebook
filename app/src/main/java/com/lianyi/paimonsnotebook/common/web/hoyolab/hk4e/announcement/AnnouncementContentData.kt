package com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement

data class AnnouncementContentData(
    val list: List<AnnouncementContentItem>,
    val pic_list: List<Any>,
    val pic_total: Int,
    val total: Int
) {
    data class AnnouncementContentItem(
        val ann_id: Int,
        val banner: String,
        val content: String,
        val lang: String,
        val subtitle: String,
        val title: String
    )
}