package com.lianyi.paimonsnotebook.common.web.hoyolab.geetest

data class GeeTestResultData<T>(
    val status: String,
    val data: T,
) {
    val success: Boolean
        get() = status == "success"
}
