package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

data class TokenByStokenData(
    val token: Token,
    val user_info: UserInfo
) {
    data class Token(
        val token: String,
        val token_type: Int
    )

    data class UserInfo(
        val account_name: String,
        val aid: String,
        val area_code: String,
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
        val safe_mobile: String
    )
}