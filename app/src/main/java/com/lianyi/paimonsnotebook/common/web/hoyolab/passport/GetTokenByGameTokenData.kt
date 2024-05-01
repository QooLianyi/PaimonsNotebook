package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

data class GetTokenByGameTokenData(
    val need_realperson: Boolean,
    val realname_info: Any,
    val token: Token,
    val user_info: UserInfo
)
