package com.lianyi.paimonsnotebook.common.web.bridge.model

import com.lianyi.paimonsnotebook.common.util.json.JSON

data class JsResult<T>(
    val retcode: Int = 0,
    val message: String = "",
    val data: T,
) : IJsResult {
    override fun toJson() = JSON.stringify(this)
}
