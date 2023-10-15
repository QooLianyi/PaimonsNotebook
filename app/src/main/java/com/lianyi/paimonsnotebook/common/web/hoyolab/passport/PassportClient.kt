package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.Cookie
import okhttp3.OkHttpClient

class PassportClient {

    suspend fun accountGetSTokenByOldToken(stokenV1: String, stuid: String, mid: String = "") =
        buildRequest {
            url(ApiEndpoints.AccountGetSTokenByOldToken)

            addHeader(
                "Cookie",
                "stoken=${stokenV1};stuid=${stuid};${if (mid.isNotBlank()) "mid=${mid}" else ""}"
            )

            addHeader("x-rpc-aigis", "")
            addHeader("x-rpc-game_biz", "bbs_cn")
            addHeader("x-rpc-sdk_version", "1.3.1.2")

            mapOf<String, String>().post(this)
        }.getAsJson<TokenByStokenData>()

    suspend fun getCookieTokenBySToken(stokenV2: Cookie) =
        buildRequest {
            url(ApiEndpoints.AccountGetCookieTokenBySToken)

            addHeader("Cookie", stokenV2.toString())
        }.getAsJson<CookieAccountInfoByStokenData>()

    suspend fun getLTokenBySToken(stokenV2: Cookie) =
        buildRequest {
            url(ApiEndpoints.AccountGetLtokenByStoken)

            addHeader("Cookie", stokenV2.toString())
        }.getAsJson<LtokenByStokenData>()

}