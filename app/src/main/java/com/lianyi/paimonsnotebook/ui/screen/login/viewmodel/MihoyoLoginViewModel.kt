package com.lianyi.paimonsnotebook.ui.screen.login.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.webkit.CookieManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.user.entity.User
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.extension.string.toMap
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.Cookie
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper.Keys as Keys
import com.lianyi.paimonsnotebook.common.web.hoyolab.passport.PassportClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth.AuthClient
import kotlinx.coroutines.launch

class MihoyoLoginViewModel : ViewModel() {

    val loginUrl = "https://user.mihoyo.com"

    private val authClient by lazy {
        AuthClient()
    }

    private val passportClient by lazy {
        PassportClient()
    }

    @SuppressLint("StaticFieldLeak")
    lateinit var activity: Activity

    //清除cookie
    private fun clearCookie() {
        CookieManager.getInstance().apply {
            removeAllCookies { }
            removeSessionCookies { }
            flush()
        }
    }

    fun checkLoginStatus() {
        viewModelScope.launch {
            checkWebView()
        }
    }

    private fun setResult(user: User) {
        clearCookie()

        val intent = Intent()
        intent.putExtra("mid", user.mid)
        activity.setResult(0, intent)
        activity.finish()
    }

    private suspend fun checkWebView() {
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(loginUrl).toMap(";", "=")

        val stuid = cookies["login_uid"] ?: cookies["ltuid"]
        val login_ticket = cookies["login_ticket"]

        if (login_ticket != null && stuid != null) {
            "正在获取所需的信息".show()

            val multiToken = authClient.getAuthMultiToken(login_ticket, stuid)

            if (!multiToken.success) {
                "获取时出现错误:multiToken-${multiToken.retcode}".show()
                return
            }

            val multiTokenMap = mutableMapOf<String, String>()
            multiToken.data.list.forEach {
                multiTokenMap[it.name] = it.token
            }
            multiTokenMap[Keys.STuid] = stuid

            AccountHelper.getUserByCookieMap(multiTokenMap,
                onFailed = {
                    it.show()
                }, onSuccess = {
                    AccountHelper.addUser(it)
                    setResult(it)
                }
            )
        }else{
            "缺少login_ticket或stuid".show()
        }

    }

}