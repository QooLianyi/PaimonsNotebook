package com.lianyi.paimonsnotebook.common.web.hoyolab.passport

import android.os.Build
import com.lianyi.core.common.util.RSAHelper
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.extension.request.setDeviceInfoHeaders
import com.lianyi.paimonsnotebook.common.extension.request.setXRpcAigis
import com.lianyi.paimonsnotebook.common.extension.request.setXRpcAppInfo
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.emptyOkHttpClient
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.Cookie

class PassportClient {

    suspend fun loginByTicket(ticket: String) =
        buildRequest {
            url(ApiEndpoints.loginByAuthTicket())

            addHeader("x-rpc-app_id", CoreEnvironment.AuthorizeKeyStarRail)

            //以下的参数都是不必要的
            addHeader("x-rpc-client_type", CoreEnvironment.ClientType)
            addHeader("x-rpc-sys_version", Build.VERSION.RELEASE)
            addHeader("x-rpc-device_fp", CoreEnvironment.DeviceFp)
            addHeader("x-rpc-device_name", "${Build.BRAND} ${Build.MODEL}")
            addHeader("x-rpc-device_id", CoreEnvironment.DeviceId)
            addHeader("x-rpc-device_model", Build.MODEL)
            addHeader("x-rpc-sdk_version", CoreEnvironment.SDKVersion)

//            addHeader("x-rpc-app_version", CoreEnvironment.XrpcVersion)

            buildMap {
                put("ticket", ticket)
            }.post(this)

        }.getAsJson<LoginResultData>(emptyOkHttpClient)


    suspend fun createLoginCaptcha(mobile: String, areaCode: String, aigis: String = "") =
        buildRequest {
            url(ApiEndpoints.createLoginCaptcha)

            if (aigis.isNotEmpty()) {
                setXRpcAigis(aigis)
            }

            buildMap {
                put("area_code", RSAHelper.encryptWithPublicKeyString(areaCode))
                put("mobile", RSAHelper.encryptWithPublicKeyString(mobile))
            }.post(this)

        }.getAsJson<CreateLoginCaptchaData>(carryResponseHeaders = true)

    suspend fun loginByMobileCaptcha(
        actionType: String,
        mobile: String,
        areaCode: String,
        code: String,
        aigis: String = ""
    ) = buildRequest {
        url(ApiEndpoints.loginByMobileCaptcha)

        if (aigis.isNotEmpty()) {
            setXRpcAigis(aigis)
        }

        buildMap {
            put("action_type", actionType)
            put("captcha", code)
            put("area_code", RSAHelper.encryptWithPublicKeyString(areaCode))
            put("mobile", RSAHelper.encryptWithPublicKeyString(mobile))
        }.post(this)

    }.getAsJson<LoginResultData>(carryResponseHeaders = true)


    suspend fun getTokenByGameToken(accountId: Int, gameToken: String) = buildRequest {
        url(ApiEndpoints.getTokenByGameToken)

        addHeader("x-rpc-game_biz", "bbs_cn")
        addHeader("x-rpc-aigis", "")
        addHeader("x-rpc-sdk_version", CoreEnvironment.SDKVersion)

        setDeviceInfoHeaders(CoreEnvironment.DeviceId40)

        buildMap {
            put("account_id", accountId)
            put("game_token", gameToken)
        }.post(this)

    }.getAsJson<GetTokenByGameTokenData>(emptyOkHttpClient)


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