package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

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
