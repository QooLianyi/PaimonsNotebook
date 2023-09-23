package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding

import java.net.URLEncoder

data class GameAuthKeyData(
    val authkey: String,
    val authkey_ver: Int,
    val sign_type: Int,
) {
    val asQueryParameter: String
        get() = "authkey=${authkey}&authkey_ver=${authkey_ver}&sign_type=${sign_type}"

    fun asEncodeAuthKeyData() = this.copy(authkey = URLEncoder.encode(authkey, "utf-8"))

}