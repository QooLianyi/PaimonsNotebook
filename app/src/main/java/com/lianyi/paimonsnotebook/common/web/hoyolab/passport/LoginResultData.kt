package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

data class LoginResultData(
    val login_ticket: String,
    val need_realperson: Boolean,
    val oauth_hw_open_id: String,
    val reactivate_info: ReactivateInfo,
    val realname_info: RealnameInfo,
    val token: Token,
    val user_info: UserInfo
) {
    data class ReactivateInfo(
        val required: Boolean,
        val ticket: String
    )

    data class RealnameInfo(
        val action_ticket: String,
        val action_type: String,
        val required: Boolean
    )

    data class Token(
        val token: String,
        val token_type: Int
    )

    data class UserInfo(
        val account_name: String,
        val aid: String,
        val area_code: String,
        val country: String,
        val email: String,
        val identity_code: String,
        val is_email_verify: Int,
        val links: List<Any>,
        val mid: String,
        val mobile: String,
        val realname: String,
        val rebind_area_code: String,
        val rebind_mobile: String,
        val rebind_mobile_time: String,
        val safe_area_code: String,
        val safe_mobile: String,
        val unmasked_email: String,
        val unmasked_email_type: Int
    )
}
