package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.annotations.JsonAdapter
import com.lianyi.paimonsnotebook.common.util.json.JSON
import java.lang.reflect.Type

data class XRpcAigisData(
    @JsonAdapter(InnerJsonDeserializer::class) val data: Data,
    val mmt_type: Int,
    val session_id: String
) {
    data class Data(
        val challenge: String,
        val gt: String,
        val new_captcha: Int,
        val success: Int
    )

    private class InnerJsonDeserializer : JsonDeserializer<Data> {
        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): Data {
            val jsonString = json?.asString ?: "{}"
            return JSON.parse<Data>(jsonString)
        }

    }
}