package com.lianyi.paimonsnotebook.common.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.activity.setImmersionMode
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.bridge.MiHoYoJSInterface
import com.lianyi.paimonsnotebook.common.web.bridge.setCookie
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData

class HoyolabWebActivity : BaseActivity() {

    companion object {
        //url
        const val EXTRA_URL = "extra_url"

        //用户mid
        const val EXTRA_MID = "mid"

        //是否在界面关闭时清理cookie
        const val EXTRA_CLEAN_COOKIE = "cleanCookie"
    }

    private val extraMid by lazy {
        intent.getStringExtra(EXTRA_MID)
    }

    private val cleanCookie by lazy {
        intent.getBooleanExtra(EXTRA_CLEAN_COOKIE, true)
    }

    //获取携带的url,默认为实时便笺界面
    private fun getExtraUrl(role: UserGameRoleData.Role): String {
        return intent?.getStringExtra(EXTRA_URL) ?: ApiEndpoints.getGenshinGameRecordUrl(role)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hoyolab_web)

        setImmersionMode()

        val webview = findViewById<WebView>(R.id.hoyolab_webview)

        val userList = AccountHelper.userListFlow.value

        val user = if (extraMid.isNullOrEmpty()) {
            AccountHelper.selectedUserFlow.value
        } else {
            userList.takeFirstIf { user: User -> user.userEntity.mid == extraMid }
        }

        if (user == null) {
            "请设置默认用户后再次尝试进入".errorNotify(false)
            finish()
            return
        }

        val role = user.getSelectedGameRole()

        if (role == null) {
            "请设置默认用户角色后再次尝试进入".errorNotify(false)
            finish()
            return
        }

        webview.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                userAgentString = CoreEnvironment.HoyolabMobileUA
            }

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    if (view == null || request == null) return false

                    view.loadUrl(request.url.toString())
                    return true
                }
            }


            addJavascriptInterface(MiHoYoJSInterface(user, webview) {
                finish()
            }, "MiHoYoJSInterface")
        }

        val url = getExtraUrl(role)

        webview.setCookie(
            cookieToken = user.userEntity.cookieToken, lToken = user.userEntity.ltoken
        )
        webview.loadUrl(url)
    }

    override fun onDestroy() {
        if (cleanCookie) {
            CookieManager.getInstance().apply {
                removeAllCookies { }
                flush()
            }
        }
        super.onDestroy()
    }

}