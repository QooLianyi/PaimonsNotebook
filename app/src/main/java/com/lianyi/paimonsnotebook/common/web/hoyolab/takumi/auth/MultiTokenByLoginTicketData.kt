package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth

data class MultiTokenByLoginTicketData(
    val list: List<Value>
) {
    data class Value(
        val name: String,
        val token: String
    )
}