package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.user

data class UserFullInfoData(
    val audit_info: AuditInfo,
    val auth_relations: List<Any>,
    val customer_service: CustomerService,
    val follow_relation: Any,
    val is_creator: Boolean,
    val is_has_collection: Boolean,
    val is_in_blacklist: Boolean,
    val user_info: UserInfo,
) {
    data class AuditInfo(
        val introduce: String,
        val is_introduce_in_audit: Boolean,
        val is_nickname_in_audit: Boolean,
        val nickname: String,
        val nickname_status: Int,
    )

    data class CustomerService(
        val game_id: Int,
        val is_customer_service_staff: Boolean,
    )

    //将用不到的字段暂时去掉了
    data class UserInfo(
//        val achieve: Achieve,
        val avatar: String,
        val avatar_url: String,
//        val certification: Certification,
//        val certifications: List<Any>,
//        val community_info: CommunityInfo,
        val gender: Int,
        val introduce: String,
//        val ip_region: String,
//        val is_logoff: Boolean,
//        val level_exp: LevelExp,
//        val level_exps: List<LevelExp>,
        val nickname: String,
        val pendant: String,
        val uid: String,
    ) {

        companion object {
            //返回一个空的占位
            fun getEmptyPlaceholder(mid:String) =
                UserInfo(
                    avatar = "",
                    avatar_url = "",
                    gender = 0,
                    introduce = "",
                    nickname = mid,
                    pendant = "",
                    uid = "Cookie失效,请重新登录"
                )
        }

        data class Achieve(
            val follow_cnt: String,
            val follow_collection_cnt: String,
            val followed_cnt: String,
            val good_post_num: String,
            val like_num: String,
            val new_follower_num: String,
            val post_num: String,
            val replypost_num: String,
            val topic_cnt: String,
        )

        data class Certification(
            val label: String,
            val type: Int,
        )

        data class CommunityInfo(
            val agree_status: Boolean,
            val created_at: Int,
            val forbid_end_time: Int,
            val forum_silent_info: List<Any>,
            val has_initialized: Boolean,
            val info_upd_time: Int,
            val is_realname: Boolean,
            val last_login_ip: String,
            val last_login_time: Int,
            val notify_disable: NotifyDisable,
            val privacy_invisible: PrivacyInvisible,
            val silent_end_time: Int,
            val user_func_status: UserFuncStatus,
        ) {
            data class NotifyDisable(
                val chat: Boolean,
                val follow: Boolean,
                val reply: Boolean,
                val system: Boolean,
                val upvote: Boolean,
            )

            data class PrivacyInvisible(
                val collect: Boolean,
                val post: Boolean,
                val post_and_instant: Boolean,
                val reply: Boolean,
                val watermark: Boolean,
            )

            data class UserFuncStatus(
                val enable_history_view: Boolean,
                val enable_mention: Boolean,
                val enable_recommend: Boolean,
                val user_center_view: Int,
            )
        }

        data class LevelExp(
            val exp: Int,
            val game_id: Int,
            val level: Int,
        )
    }

}