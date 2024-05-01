package com.lianyi.paimonsnotebook.common.web.bridge

import android.os.Build
import android.webkit.JavascriptInterface
import android.webkit.WebView
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.extension.intent.setComponentName
import com.lianyi.paimonsnotebook.common.util.hoyolab.DynamicSecret
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.view.HoyolabWebActivity
import com.lianyi.paimonsnotebook.common.web.bridge.model.ActionTypePayload
import com.lianyi.paimonsnotebook.common.web.bridge.model.CookieTokenPayload
import com.lianyi.paimonsnotebook.common.web.bridge.model.DynamicSecrect2Payload
import com.lianyi.paimonsnotebook.common.web.bridge.model.IJsResult
import com.lianyi.paimonsnotebook.common.web.bridge.model.JsParams
import com.lianyi.paimonsnotebook.common.web.bridge.model.JsResult
import com.lianyi.paimonsnotebook.common.web.bridge.model.PushPagePayload
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth.AuthClient
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MiHoYoJSInterface(
    private val user: User,
    private val webView: WebView,
    private val closePage: () -> Unit = {}
) {

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
        val actionTicketData =
            authClient.getActionTicketBySToken(user.userEntity, params.payload.actionType)
        return JsResult(
            retcode = actionTicketData.retcode,
            message = actionTicketData.message,
            data = actionTicketData.data
        )
    }

    private fun getCookieInfo(params: JsParams<Any?>): IJsResult {
        return JsResult(
            data = mapOf(
                CookieHelper.Keys.LTuid to user.userEntity.ltoken[CookieHelper.Keys.LTuid],
                CookieHelper.Keys.LToken to user.userEntity.ltoken[CookieHelper.Keys.LToken],
                CookieHelper.Keys.LoginTicket to ""
            )
        )
    }

    private fun getCookieToken(params: JsParams<CookieTokenPayload>): IJsResult {
        val cookieToken = user.userEntity.cookieToken

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
                "DS" to DynamicSecret.getDynamicSecret(
                    DynamicSecret.Version.Gen1,
                    DynamicSecret.SaltType.LK2
                ),
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
                "DS" to DynamicSecret.getDynamicSecret(
                    DynamicSecret.Version.Gen2,
                    DynamicSecret.SaltType.X4,
                    query = q,
                    body = b
                )
            )
        )
    }

    private fun getHTTPRequestHeaders(params: JsParams<Any?>): IJsResult {
        return JsResult(
            data = mapOf(
                "x-rpc-client_type" to CoreEnvironment.ClientType,
                "x-rpc-device_id" to CoreEnvironment.DeviceId,
                "x-rpc-app_version" to CoreEnvironment.XrpcVersion,
                "x-rpc-app_id" to "bll8iq97cem8",
                "x-rpc-sdk_version" to "2.20.2",
                "x-rpc-device_fp" to CoreEnvironment.DeviceFp,
                "Content-Type" to "application/json",

                "x-rpc-device_name" to Build.DEVICE,
                "x-rpc-device_model" to Build.MODEL,
                "x-rpc-sys_version" to Build.VERSION.RELEASE,
            )
        )
    }

    private fun getStatusBarHeight(params: JsParams<Any?>): IJsResult {
        return JsResult(
            //始终将状态栏的高度设置为24
            data = mapOf(
//                "statusBarHeight" to SystemService.statusBarHeight,
                "statusBarHeight" to 24,
            )
        )
    }

    private fun getUserInfo(params: JsParams<Any?>): IJsResult {
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

    private fun pushPage(payload: PushPagePayload): JsResult<Map<String, Any>>? {
        HomeHelper.goActivityByIntentNewTask {
            setComponentName(HoyolabWebActivity::class.java)
            putExtra(HoyolabWebActivity.EXTRA_URL, payload.page)
            putExtra(HoyolabWebActivity.EXTRA_MID, user.userEntity.mid)
            putExtra(HoyolabWebActivity.EXTRA_CLEAN_COOKIE, false)
        }
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
            "pushPage" -> {
                val payload = JSON.parse<PushPagePayload>(JSON.stringify(param.payload))
                pushPage(payload)
            }

            "showLoading" -> null
            else -> {
                null
            }
        }
    }

    private fun callbackScript(callback: String, payload: String? = null) {

        println("callback = ${callback}")

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

        println("param = ${str}")

        CoroutineScope(Dispatchers.IO).launch {
            val result = tryGetJsResultFromJsParam(param)

            if (result != null && !param.callback.isNullOrBlank()) {
                callbackScript(param.callback, result.toJson())
            }
        }

    }
}