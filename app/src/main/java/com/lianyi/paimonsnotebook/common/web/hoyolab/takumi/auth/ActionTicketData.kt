package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth

data class ActionTicketData(
    val account_info: AccountInfo,
    val is_verified: Boolean,
    val ticket: String
) {
    data class AccountInfo(
        val account_id: String,
        val account_name: String,
        val apple_name: String,
        val area_code: String,
        val black_endtime: String,
        val black_note: String,
        val change_pwd_time: String,
        val create_ip: String,
        val create_time: String,
        val email: String,
        val gender: Int,
        val identity_code: String,
        val is_email_verify: Boolean,
        val is_realname: Boolean,
        val mobile: String,
        val nickname: String,
        val reactivate_ticket: String,
        val real_name: String,
        val real_stat: Int,
        val safe_area_code: String,
        val safe_level: Int,
        val safe_mobile: String,
        val sony_name: String,
        val tap_name: String,
        val user_icon_id: Int
    )
}