package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

data class CreateLoginCaptchaData(
    val action_type: String,
    val countdown: Int,
    val sent_new: Boolean
)