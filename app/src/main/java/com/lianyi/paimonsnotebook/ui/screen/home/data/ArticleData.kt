package com.lianyi.paimonsnotebook.ui.screen.home.data

data class ArticleData(
    val post: Post
) {
    data class Post(
        val collection: Any,
        val cover: Any,
        val forum: Forum,
        val forum_rank_info: Any,
        val help_sys: Any,
        val hot_reply_exist: Boolean,
        val image_list: List<Image>,
        val is_block_on: Boolean,
        val is_official_master: Boolean,
        val is_user_master: Boolean,
        val last_modify_time: Int,
        val link_card_list: List<Any>,
        val post: Post,
        val recommend_type: String,
        val self_operation: SelfOperation,
        val stat: Stat,
        val topics: List<Topic>,
        val user: User,
        val vod_list: List<Vod>,
        val vote_count: Int
    ) {
        data class Forum(
            val forum_cate: Any,
            val game_id: Int,
            val icon: String,
            val id: Int,
            val name: String
        )

        data class Image(
            val crop: Any,
            val entity_id: String,
            val entity_type: String,
            val format: String,
            val height: Int,
            val image_id: String,
            val is_user_set_cover: Boolean,
            val size: String,
            val url: String,
            val width: Int
        )

        data class Post(
            val cate_id: Int,
            val content: String,
            val cover: String,
            val created_at: Int,
            val deleted_at: Int,
            val f_forum_id: Int,
            val game_id: Int,
            val images: List<String>,
            val is_deleted: Int,
            val is_in_profit: Boolean,
            val is_interactive: Boolean,
            val is_original: Int,
            val is_profit: Boolean,
            val max_floor: Int,
            val post_id: String,
            val post_status: PostStatus,
            val pre_pub_status: Int,
            val reply_time: String,
            val republish_authorization: Int,
            val review_id: Int,
            val structured_content: String,
            val structured_content_rows: List<Any>,
            val subject: String,
            val topic_ids: List<Int>,
            val uid: String,
            val updated_at: Int,
            val view_status: Int,
            val view_type: Int
        ) {
            data class PostStatus(
                val is_good: Boolean,
                val is_official: Boolean,
                val is_top: Boolean
            )
        }

        data class SelfOperation(
            val attitude: Int,
            val is_collected: Boolean
        )

        data class Stat(
            val bookmark_num: Int,
            val forward_num: Int,
            val like_num: Int,
            val reply_num: Int,
            val view_num: Int
        )

        data class Topic(
            val content_type: Int,
            val cover: String,
            val game_id: Int,
            val id: Int,
            val is_good: Boolean,
            val is_interactive: Boolean,
            val is_top: Boolean,
            val name: String
        )

        data class User(
            val avatar: String,
            val avatar_url: String,
            val certification: Certification,
            val gender: Int,
            val introduce: String,
            val is_followed: Boolean,
            val is_following: Boolean,
            val level_exp: LevelExp,
            val nickname: String,
            val pendant: String,
            val uid: String
        ) {
            data class Certification(
                val label: String,
                val type: Int
            )

            data class LevelExp(
                val exp: Int,
                val level: Int
            )
        }

        data class Vod(
            val cover: String,
            val duration: Int,
            val id: String,
            val resolutions: List<Resolution>,
            val review_status: Int,
            val transcoding_status: Int,
            val view_num: Int
        ) {
            data class Resolution(
                val bitrate: Int,
                val definition: String,
                val format: String,
                val height: Int,
                val label: String,
                val size: String,
                val url: String,
                val width: Int
            )
        }
    }
}