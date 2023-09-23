package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs

data class WebHomeData(
    val carousels: List<Carousel>,
    val fixed_posts: List<Any>,
    val recommended_posts: List<RecommendedPost>,
    val recommended_topics: RecommendedTopics,
    val selection_post_list: List<SelectionPost>
) {
    data class Carousel(
        val cover: String,
        val path: String
    )

    data class RecommendedPost(
        val collection: Any,
        val cover: Cover,
        val forum: Forum,
        val help_sys: HelpSys,
        val image_list: List<Image>,
        val is_official_master: Boolean,
        val is_user_master: Boolean,
        val last_modify_time: Int,
        val post: Post,
        val recommend_type: String,
        val self_operation: Any,
        val stat: Stat,
        val topics: List<Topic>,
        val user: User,
        val vod_list: List<Vod>,
        val vote_count: Int
    ) {
        data class Cover(
            val crop: Crop,
            val entity_id: String,
            val entity_type: String,
            val format: String,
            val height: Int,
            val image_id: String,
            val is_user_set_cover: Boolean,
            val size: String,
            val url: String,
            val width: Int
        ) {
            data class Crop(
                val h: Int,
                val url: String,
                val w: Int,
                val x: Int,
                val y: Int
            )
        }

        data class Forum(
            val id: Int,
            val name: String
        )

        data class HelpSys(
            val top_up: Any
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
            val content: String,
            val cover: String,
            val created_at: Int,
            val f_forum_id: Int,
            val game_id: Int,
            val images: List<String>,
            val is_deleted: Int,
            val is_interactive: Boolean,
            val is_original: Int,
            val max_floor: Int,
            val post_id: String,
            val post_status: PostStatus,
            val reply_time: String,
            val republish_authorization: Int,
            val score: Int,
            val subject: String,
            val topic_ids: List<Int>,
            val uid: String,
            val view_status: Int,
            val view_type: Int
        ) {
            data class PostStatus(
                val is_good: Boolean,
                val is_official: Boolean,
                val is_top: Boolean
            )
        }

        data class Stat(
            val bookmark_num: Int,
            val like_num: Int,
            val reply_num: Int,
            val view_num: Int
        )

        data class Topic(
            val content_type: Int,
            val cover: String,
            val id: Int,
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

    data class RecommendedTopics(
        val list: List<RecommendedTopicsItem>,
        val position: Int
    ) {
        data class RecommendedTopicsItem(
            val cover: String,
            val desc: String,
            val discuss_num: Int,
            val id: Int,
            val is_focus: Boolean,
            val name: String,
            val view_num: Int
        )
    }

    data class SelectionPost(
        val banner: String,
        val forum_id: Int,
        val forum_name: String,
        val post_id: String,
        val subject: String,
        val view_type: Int
    )
}