package com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe.topic

data class TopicInfoData(
    val game_info_list: List<GameInfo>,
    val good_exist: Boolean,
    val good_post_cnt: Int,
    val good_post_exist: Boolean,
    val hot_post_cnt: Int,
    val related_forums: List<Any>,
    val related_topics: List<Any>,
    val top_posts: List<Any>,
    val topic: Topic
){
    data class GameInfo(
        val has_good: Boolean,
        val has_hot: Boolean,
        val id: Int,
        val name: String
    )

    data class Topic(
        val alias: List<String>,
        val content_type: Int,
        val cover: String,
        val created_at: String,
        val creator: String,
        val creator_type: Int,
        val default_game_id: Int,
        val desc: String,
        val game_id: Int,
        val good_cnt: Int,
        val id: String,
        val is_deleted: Int,
        val is_focus: Boolean,
        val is_interactive: Boolean,
        val name: String,
        val order: Int,
        val related_forum_ids: RelatedForumIds,
        val stat: Any,
        val topic_sort_config: List<TopicSortConfig>,
        val topic_type: Int,
        val updated_at: String,
        val view_type: List<Int>
    )

    class RelatedForumIds

    data class TopicSortConfig(
        val data_report_name: String,
        val name: String,
        val show_sort: Int,
        val type: Int,
        val url: String
    )
}