package com.lianyi.paimonsnotebook.common.core.client

import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.data.client.LastReleaseInfoData
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.emptyOkHttpClient
import com.lianyi.paimonsnotebook.common.util.request.getAsTextResult

/*
* 集合程序中运行所需的api请求
* */
class ApplicationClient {

    suspend fun getLatestReleaseInfo(): Pair<Boolean, LastReleaseInfoData> =
        buildRequest {
            url(PaimonsNotebookApplication.latestReleaseUrl)
        }.getAsTextResult(emptyOkHttpClient).let {
            it.first to if (it.first) {
                JSON.parse(it.second)
            } else {
                JSON.parse("{}")
            }
        }

}