package com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.event.gacha_info

import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints

class GachaInfoClient {

    suspend fun getGachaLogPage(configData: GachaQueryConfigData) =
        buildRequest {
            url(ApiEndpoints.GachaInfoGetGachaLog(configData.asQueryParameter))
        }.getAsJson<GachaLogData>()

}