package com.lianyi.paimonsnotebook.common.web.bridge.model

data class JsParams<T>(
    val method:String,
    val payload:T,
    val callback:String
)
