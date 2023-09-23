package com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.common

import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints

class TakumiCommonClient {

    suspend fun getGachaPool() = buildRequest {
        url(ApiEndpoints.GachaPool)
    }.getAsJson<GachaPoolData>()

}