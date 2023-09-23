package com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement


data class AnnouncementData(
    val alert: Boolean,
    val alert_id: Int,
    val list: List<AnnouncementList>,
    val pic_alert: Boolean,
    val pic_alert_id: Int,
    val pic_list: List<Any>,
    val pic_total: Int,
    val pic_type_list: List<Any>,
    val static_sign: String,
    val t: String,
    val timezone: Int,
    val total: Int,
    val type_list: List<Type>
) {
    data class AnnouncementList(
        val list: List<AnnouncementItem>,
        val type_id: Int,
        val type_label: String
    ) {
        data class AnnouncementItem(
            val alert: Int,
            val ann_id: Int,
            val banner: String,
            val content: String,
            val end_time: String,
            val extra_remind: Int,
            val has_content: Boolean,
            val lang: String,
            val login_alert: Int,
            val remind: Int,
            val remind_ver: Int,
            val start_time: String,
            val subtitle: String,
            val tag_end_time: String,
            val tag_icon: String,
            val tag_label: String,
            val tag_start_time: String,
            val title: String,
            val type: Int,
            val type_label: String
        )
    }

    data class Type(
        val id: Int,
        val mi18n_name: String,
        val name: String
    )
}
