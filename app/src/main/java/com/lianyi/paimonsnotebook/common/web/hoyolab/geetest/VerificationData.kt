package com.lianyi.paimonsnotebook.common.web.hoyolab.geetest

data class VerificationData(
    val challenge: String,
    val gt: String,
    val new_captcha: Int,
    val success: Int
)