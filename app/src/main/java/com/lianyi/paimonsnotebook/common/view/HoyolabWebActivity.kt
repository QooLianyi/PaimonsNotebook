package com.lianyi.paimonsnotebook.common.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.common.web.bridge.MiHoYoJSInterface
import com.lianyi.paimonsnotebook.common.web.bridge.setCookie
import kotlinx.coroutines.launch

class HoyolabWebActivity : AppCompatActivity() {

    companion object{
        const val WEB_VIEW_URL = "MIYOUSHE_URL"
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
            AccountHelper.selectedUserFlow.collect { user ->

                println("${user?.userEntity?.isSelected} MiHoYoJSInterface = ${user}")

                if (user != null) {
                    val role = user.userGameRoles.firstOrNull { it.is_chosen }

                    if (role != null) {
                        val query = "role_id=${role.game_uid}&server=${role.region}"
//                        val url =
//                            "https://webstatic.mihoyo.com/app/community-game-records/rpg/?game_id=6#/"
                        val url =
                            "https://webstatic.mihoyo.com/app/community-game-records/index.html?bbs_presentation_style=fullscreen#/ys/daily/?${query}"

                        webview.setCookie(cookieToken = user.userEntity.cookieToken, lToken = user.userEntity.ltoken)

                        webview.loadUrl(url)
                    }else{
                        "当前账号没有设置游戏角色".errorNotify()
                    }
                }
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