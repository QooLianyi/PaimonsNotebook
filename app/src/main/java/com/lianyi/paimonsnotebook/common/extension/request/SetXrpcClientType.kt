package com.lianyi.paimonsnotebook.common.extension.request

import okhttp3.Request

fun Request.Builder.setXrpcClientType(value:String) = this.header("x-rpc-client_type",value)