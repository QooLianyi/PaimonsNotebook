package com.lianyi.paimonsnotebook.common.extension.request

import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.AccountData
import com.lianyi.paimonsnotebook.common.extension.map.cookieString
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import okhttp3.Request

fun Request.Builder.setUser(accountData: AccountData, cookieType: Int) {

    val stringBuild = StringBuilder()

    if ((cookieType and CookieHelper.Type.CookieToken) == CookieHelper.Type.CookieToken) {
        stringBuild.append(accountData.CookieToken.cookieString)
    }
    if ((cookieType and CookieHelper.Type.Stoken) == CookieHelper.Type.Stoken) {
        stringBuild.append(accountData.Stoken.cookieString)
    }
    if ((cookieType and CookieHelper.Type.Ltoken) == CookieHelper.Type.Ltoken) {
        stringBuild.append(accountData.Ltoken.cookieString)
    }

    this.addHeader("Cookie", stringBuild.toString())
}

fun Request.Builder.setUser(user: User, cookieType: Int) {

    val stringBuild = StringBuilder()

    if ((cookieType and CookieHelper.Type.CookieToken) == CookieHelper.Type.CookieToken) {
        stringBuild.append(user.cookieToken.toString())
    }
    if ((cookieType and CookieHelper.Type.Stoken) == CookieHelper.Type.Stoken) {
        stringBuild.append(user.stoken.toString())
    }
    if ((cookieType and CookieHelper.Type.Ltoken) == CookieHelper.Type.Ltoken) {
        stringBuild.append(user.ltoken.toString())
    }

    this.addHeader("Cookie", stringBuild.toString())
}
