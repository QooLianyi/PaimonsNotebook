package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
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
            addHeader("x-rpc-app_id", "bll8iq97cem8")
            addHeader("x-rpc-client_type", "2")

            addHeader("User-Agent", CoreEnvironment.HoyolabMobileUA)
            addHeader("x-rpc-device_id", CoreEnvironment.DeviceId)
            addHeader("x-rpc-app_version", CoreEnvironment.XrpcVersion)

            mapOf<String, String>().post(this)
        }.getAsJson<TokenByStokenData>(OkHttpClient())

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