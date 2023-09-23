package com.lianyi.paimonsnotebook.common.web.hoyolab.bbs

import com.lianyi.paimonsnotebook.common.extension.request.setReferer
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.post.PostFullData

class BbsApiClient {

    suspend fun getOfficialRecommendedPosts() =
        buildRequest {
            url(ApiEndpoints.OfficialRecommendedPosts)
        }.getAsJson<OfficialRecommendedPostsData>()

    suspend fun getPostFull(postId: Long) = buildRequest {
        url(ApiEndpoints.getPostFull(postId))

        setReferer("https://bbs.mihoyo.com/")
    }.getAsJson<PostFullData>()

}