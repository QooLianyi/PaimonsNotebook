package com.lianyi.paimonsnotebook.common.extension.request

import okhttp3.Request

fun Request.Builder.setXrpcChallenge(value:String) = this.addHeader("x-rpc-challenge",value)