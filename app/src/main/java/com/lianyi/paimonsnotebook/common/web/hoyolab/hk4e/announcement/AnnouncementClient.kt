package com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement

import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints

class AnnouncementClient {

    suspend fun getAnnList() =
        buildRequest {
            url(ApiEndpoints.AnnList)
        }.getAsJson<AnnouncementData>()

    suspend fun getAnnContent() =
        buildRequest {
            url(ApiEndpoints.AnnContent)
        }.getAsJson<AnnouncementContentData>()

}