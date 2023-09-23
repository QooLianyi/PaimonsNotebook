package com.lianyi.paimonsnotebook.common.web.uigf

import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsTextResult
import com.lianyi.paimonsnotebook.common.web.UIGFApiEndpoints

class UIGFClient {

    //获取UIGF Item字典
    suspend fun getUIGFDictJson(): String {
        val result = buildRequest {
            url(UIGFApiEndpoints.getUIGFItemDictJsonUrl())
        }.getAsTextResult()

        if (!result.first) {
            return ""
        }
        return result.second
    }

    //判断当前本地缓存的map是否需要更新
    suspend fun validateUIGFDictMD5Value(md5: String): Boolean {
        val result = buildRequest {
            url(UIGFApiEndpoints.getUIGFItemMD5ValidateMapUrl())
        }.getAsTextResult()

        return if (result.first) {
            val map = JSON.parse<Map<String, String>>(
                result.second,
                getParameterizedType(Map::class.java, String::class.java, String::class.java)
            )

            md5 == (map["chs"] ?: "")
        } else {
            false
        }
    }

}