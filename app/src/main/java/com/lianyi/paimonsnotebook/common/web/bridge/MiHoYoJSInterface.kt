package com.lianyi.paimonsnotebook.common.web.bridge

import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.bridge.model.*
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth.AuthClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MiHoYoJSInterface(private val webView: WebView, private val closePage: () -> Unit = {}) {

    private val authClient by lazy {
        AuthClient()
    }

    private fun closePage(params: JsParams<Any?>): JsResult<Map<String, Any>>? {
        CoroutineScope(Dispatchers.Main).launch {
            webView.goBack()
            if (!webView.canGoBack()) {
                closePage.invoke()
            }
        }
        return null
    }

    private fun configureShare(params: JsParams<Any?>) = null

    private suspend fun getActionTicket(params: JsParams<ActionTypePayload>): IJsResult {
        val user = AccountHelper.selectedUserFlow.value!!

        val actionTicketData =
            authClient.getActionTicketBySToken(user.userEntity, params.payload.actionType)
        return JsResult(
            retcode = actionTicketData.retcode,
            message = actionTicketData.message,
            data = actionTicketData.data
        )
    }

    private fun getCookieInfo(params: JsParams<Any?>): IJsResult {
        val user = AccountHelper.selectedUserFlow.value!!.userEntity

        return JsResult(
            data = mapOf(
                CookieHelper.Keys.LTuid to user.ltoken[CookieHelper.Keys.LTuid],
                CookieHelper.Keys.LToken to user.ltoken[CookieHelper.Keys.LToken],
                CookieHelper.Keys.LoginTicket to ""
            )
        )
    }

    private fun getCookieToken(params: JsParams<CookieTokenPayload>): IJsResult {
        val user = AccountHelper.selectedUserFlow.value!!.userEntity

        val cookieToken = user.cookieToken

        return JsResult(
            data = mapOf(
                CookieHelper.Keys.CookieToken to cookieToken[CookieHelper.Keys.CookieToken]
            )
        )
    }

    private fun getCurrentLocale(params: JsParams<Any?>): IJsResult {
        return JsResult(
            data = mapOf(
                "language" to "zh-CN",
                "timeZone" to "GMT+8"
            )
        )
    }

    private fun getDS(params: JsParams<Any?>): IJsResult {
        return JsResult(
            data = mapOf(
                "DS" to DynamicSecret.getDynamicSecret(DynamicSecret.Version.Gen1,DynamicSecret.SaltType.LK2),
            )
        )
    }

    private fun getRandomString(): String {
        val range = "abcdefghijklmnopqrstuvwxyz1234567890"
        with(StringBuilder()) {
            repeat(6) {
                this.append(range.random())
            }
            return this.toString()
        }
    }

    private fun getDS2(params: JsParams<DynamicSecrect2Payload>): IJsResult {
        val b = params.payload.body
        val q = params.payload.getQueryParam()
        return JsResult(
            data = mapOf(
                "DS" to DynamicSecret.getDynamicSecret(DynamicSecret.Version.Gen2,DynamicSecret.SaltType.X4, query = q, body = b)
            )
        )
    }

    private fun getHTTPRequestHeaders(params: JsParams<Any?>): IJsResult {
        return JsResult(
            data = mapOf(
                "x-rpc-client_type" to "",
                "x-rpc-device_id" to CoreEnvironment.DeviceId,
                "x-rpc-app_version" to CoreEnvironment.XrpcVersion
            )
        )
    }

    private fun getStatusBarHeight(params: JsParams<Any?>): IJsResult {
        return JsResult(
            data = mapOf(
                "statusBarHeight" to 0,
            )
        )
    }

    private fun getUserInfo(params: JsParams<Any?>): IJsResult {
        val user = AccountHelper.selectedUserFlow.value!!
        val info = user.userInfo
        return JsResult(
            data = mapOf(
                "id" to info.uid,
                "gender" to info.gender,
                "nickname" to info.nickname,
                "introduce" to info.introduce,
                "avatar_url" to info.avatar_url
            )
        )
    }

    private fun pushPage(params: JsParams<PushPagePayload>): JsResult<Map<String, Any>>? {
        webView.loadUrl(params.payload.page)
        return null
    }

    private fun eventTrack(param: JsParams<Any?>): IJsResult? {
        return null
    }

    private suspend fun tryGetJsResultFromJsParam(
        param: JsParams<Any?>,
    ): IJsResult? {

        return when (param.method) {
            "closePage" -> closePage(param)
            "configure_share" -> configureShare(param)
            "eventTrack" -> eventTrack(param)
            "getActionTicket" -> getActionTicket(
                JsParams(param.method, JSON.parse(JSON.stringify(param.payload)), param.callback)
            )
            "getCookieInfo" -> getCookieInfo(param)
            "getCookieToken" -> getCookieToken(
                JsParams(param.method, JSON.parse(JSON.stringify(param.payload)), param.callback)
            )
            "getCurrentLocale" -> getCurrentLocale(param)
            "getDS" -> getDS(param)
            "getDS2" ->
                getDS2(
                    JsParams(
                        param.method,
                        JSON.parse(JSON.stringify(param.payload)),
                        param.callback
                    )
                )
            "getHTTPRequestHeaders" -> getHTTPRequestHeaders(param)
            "getStatusBarHeight" -> getStatusBarHeight(param)
            "getUserInfo" -> getUserInfo(param)
            "hideLoading" -> null
            "login" -> null
            "pushPage" -> pushPage(
                JsParams(param.method, JSON.parse(JSON.stringify(param.payload)), param.callback)
            )
            "showLoading" -> null
            else -> {
                null
            }
        }
    }

    private fun callbackScript(callback: String, payload: String? = null) {
        if (callback.isBlank()) {
            return
        }

        val js =
            "javascript:mhyWebBridge(\"${callback}\"${if (payload != null) ",${payload}" else ""})"

        CoroutineScope(Dispatchers.Main).launch {
            webView.loadUrl(js)
        }
    }

    @JavascriptInterface
    fun postMessage(str: String) {
        val param = JSON.parse<JsParams<Any?>>(str)

        CoroutineScope(Dispatchers.IO).launch {
            val result = tryGetJsResultFromJsParam(param)

            if (result != null && !param.callback.isNullOrBlank()) {
                callbackScript(param.callback, result.toJson())
            }
        }

    }
}