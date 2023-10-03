package com.lianyi.paimonsnotebook.common.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.extension.string.showLong
import com.lianyi.paimonsnotebook.common.web.bridge.MiHoYoJSInterface
import com.lianyi.paimonsnotebook.common.web.bridge.setCookie
import kotlinx.coroutines.launch

class HoyolabWebActivity : AppCompatActivity() {

    companion object {
        const val WEB_VIEW_URL = "MIYOUSHE_URL"
        const val PARAM_MID = "mid"
    }

    private val extraMid by lazy {
        intent.getStringExtra(PARAM_MID)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hoyolab_web)

        val webview = findViewById<WebView>(R.id.hoyolab_webview)

        webview.apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                userAgentString = CoreEnvironment.HoyolabMobileWebUA
            }

            addJavascriptInterface(MiHoYoJSInterface(webview) {
                finish()
            }, "MiHoYoJSInterface")
        }

        lifecycleScope.launch {
            AccountHelper.userListFlow.collect { userList ->
                val user = if (extraMid.isNullOrEmpty()) {
                    userList.takeFirstIf { user -> user.isSelected }
                } else {
                    userList.takeFirstIf { user: User -> user.userEntity.mid == extraMid }
                }
                println("extraMid = ${extraMid}")

                val role = user?.getSelectedGameRole()
                if (user == null || role == null) {
                    "用户或角色不存在".showLong()
                    finish()
                    return@collect
                }

                val query = "role_id=${role.game_uid}&server=${role.region}"
                val url =
                    "https://webstatic.mihoyo.com/app/community-game-records/index.html?bbs_presentation_style=fullscreen#/ys/daily/?${query}"

                webview.setCookie(
                    cookieToken = user.userEntity.cookieToken,
                    lToken = user.userEntity.ltoken
                )
                webview.loadUrl(url)
            }
        }
    }

    override fun onDestroy() {
        CookieManager.getInstance().apply {
            removeAllCookies { }
            flush()
        }
        super.onDestroy()
    }

}