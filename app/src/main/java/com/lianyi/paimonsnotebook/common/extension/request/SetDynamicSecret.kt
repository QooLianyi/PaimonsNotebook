package com.lianyi.paimonsnotebook.common.extension.request

import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import okhttp3.Request

fun Request.Builder.setDynamicSecret(
    saltType: DynamicSecret.SaltType,
    version: DynamicSecret.Version = DynamicSecret.Version.Gen1,
    includeChars: Boolean = false,
    query: String = "",
    body: String = "",
) {
    val build = this.build()
    val urls = build.url.toString().split("?")

    val b = if (saltType == DynamicSecret.SaltType.PROD) "{}" else body

    if (urls.size > 1) {
        val parameters = urls.last().split("&").sortedBy { it }.joinToString(separator = "&") { it }
        this.addHeader("DS",
            DynamicSecret.getDynamicSecret(version,
                saltType,
                includeChars,
                parameters,
                b))
    } else {
        this.addHeader("DS",
            DynamicSecret.getDynamicSecret(version,
                saltType,
                includeChars,
                query,
                b))
    }
}