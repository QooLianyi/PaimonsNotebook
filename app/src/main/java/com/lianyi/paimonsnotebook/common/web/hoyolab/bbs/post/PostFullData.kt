package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post

data class PostFullData(
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
        val is_mentor: Boolean,
        val is_official_master: Boolean,
        val is_user_master: Boolean,
        val last_modify_time: Int,
        val link_card_list: List<LinkCard>,
        val news_meta: Any,
        val post: Post,
        val recommend_reason: RecommendReason,
        val recommend_type: String,
        val self_operation: SelfOperation,
        val stat: Stat,
        val topics: List<Topic>,
        val user: User,
        val villa_card: Any,
        val vod_list: List<Vod>,
        val vote_count: Int
    ) {
        data class Vod(
            val brief_intro: String,
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
            val is_deleted: Boolean,
            val is_user_set_cover: Boolean,
            val size: String,
            val url: String,
            val width: Int
        )

        data class LinkCard(
            val button_text: String,
            val card_id: String,
            val card_meta: Any,
            val card_status: Int,
            val cover: String,
            val landing_url: String,
            val landing_url_type: Int,
            val link_type: Int,
            val market_price: String,
            val origin_url: String,
            val origin_user_avatar: String,
            val origin_user_avatar_url: String,
            val origin_user_nickname: String,
            val price: String,
            val title: String
        )

        data class Post(
            val audit_status: Int,
            val block_latest_reply_time: Int,
            val block_reply_img: Int,
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
            val is_mentor: Boolean,
            val is_missing: Boolean,
            val is_original: Int,
            val is_profit: Boolean,
            val is_showing_missing: Boolean,
            val max_floor: Int,
            val meta_content: String,
            val post_id: String,
            val post_status: PostStatus,
            val pre_pub_status: Int,
            val profit_post_status: Int,
            val reply_time: String,
            val republish_authorization: Int,
            val review_id: Int,
            val selected_comment: Int,
            val structured_content: String,
            val structured_content_rows: List<Any>,
            val subject: String,
            val topic_ids: List<Int>,
            val uid: String,
            val updated_at: Int,
            val view_status: Int,
            val view_type: Int
        )

        data class RecommendReason(
            val is_mentor_rec_block: Boolean,
            val tags: List<Tag>
        )

        data class SelfOperation(
            val attitude: Int,
            val is_collected: Boolean,
            val upvote_type: Int
        )

        data class Stat(
            val bookmark_num: Int,
            val forward_num: Int,
            val like_num: Int,
            val original_like_num: Int,
            val post_upvote_stat: List<PostUpvoteStat>,
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
            val certifications: List<Certification>,
            val gender: Int,
            val introduce: String,
            val is_creator: Boolean,
            val is_followed: Boolean,
            val is_following: Boolean,
            val level_exp: LevelExp,
            val nickname: String,
            val pendant: String,
            val uid: String
        )

        data class PostStatus(
            val is_good: Boolean,
            val is_official: Boolean,
            val is_top: Boolean,
            val post_status: Int
        )

        data class Tag(
            val deep_link: String,
            val text: String,
            val type: String
        )

        data class PostUpvoteStat(
            val upvote_cnt: Int,
            val upvote_type: Int
        )

        data class Certification(
            val label: String,
            val type: Int
        )

        data class LevelExp(
            val exp: Int,
            val level: Int
        )
    }
}