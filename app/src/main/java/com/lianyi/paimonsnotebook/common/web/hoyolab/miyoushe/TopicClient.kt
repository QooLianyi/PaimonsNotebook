package com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe

import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe.painter_topic.PainterTopicListData
import com.lianyi.paimonsnotebook.common.web.hoyolab.miyoushe.topic.TopicInfoData

class TopicClient {

    suspend fun getTopicInfo(topicId: Long) =
        buildRequest {
            url(ApiEndpoints.getTopicInfo(topicId))
        }.getAsJson<TopicInfoData>()

    suspend fun getPainterTopicList(
        topicId: Long,
        listType: String = "UNKNOWN",
        offset: String = "",
        size: Int = 20,
        gameId: Int = 0
    ) =
        buildRequest {
            url(ApiEndpoints.getPainterTopicList(topicId, listType, offset, size, gameId))
        }.getAsJson<PainterTopicListData>()

}