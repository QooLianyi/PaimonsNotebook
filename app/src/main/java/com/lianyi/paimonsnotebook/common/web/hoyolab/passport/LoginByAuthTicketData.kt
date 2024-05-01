package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

data class LoginByAuthTicketData(
    val login_ticket: String,
    val need_realperson: Boolean,
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
}