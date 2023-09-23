package com.lianyi.paimonsnotebook.common.web.bridge.model

data class DynamicSecrect2Payload(
    val query: Map<String, Any>,
    val body: String,
) {
    fun getQueryParam() = query.map {
        "${it.key}=${it.value}"
    }.joinToString(separator = "&") { it }
}
