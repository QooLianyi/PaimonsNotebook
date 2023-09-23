package com.lianyi.paimonsnotebook.common.web.bridge

import android.webkit.CookieManager
import android.webkit.WebView
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.Cookie

fun WebView.setCookie(
    cookieToken: Cookie? = null,
    lToken: Cookie? = null,
    sToken: Cookie? = null,
) {
    val url = ".mihoyo.com"

    val manager = CookieManager.getInstance()

    listOf(
        cookieToken,
        lToken,
        sToken
    ).forEach {
        it?.foreach { s, s2 ->
            manager.setCookie(url, "${s}=${s2}")
        }
    }
    manager.flush()
}