package com.lianyi.paimonsnotebook.common.extension.request

import okhttp3.Request

fun Request.Builder.setReferer(value: String) = this.addHeader("Referer", value)