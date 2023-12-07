package com.lianyi.paimonsnotebook.common.extension.request

import okhttp3.Headers
import okhttp3.Request

fun Request.Builder.setHeaders(value: Headers) = this.headers(headers = value)