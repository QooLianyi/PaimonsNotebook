package com.lianyi.paimonsnotebook.ui.screen.account.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.AccountData
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.web.hoyolab.cookie.CookieHelper
import com.lianyi.paimonsnotebook.ui.screen.home.view.HomeScreen
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val WEB_URL = "https://user.miyoushe.com/login-platform/mobile.html?game_biz=bbs_cn&app_id=bll8iq97cem8&token_type=4&app_version=2.40.1&environment=production&redirect_url=https%253A%252F%252Fm.miyoushe.com%252Fys%252F%2523%252Fhome%252F0&sync_login_status=1&platform=5&st=https%253A%252F%252Fm.miyoushe.com%252Fys%252F%2523%252Fhome%252F0#/login/captcha"

    val context by lazy {
        PaimonsNotebookApplication.context
    }

    var accountLogin by mutableStateOf(false)

    var cookieInput by mutableStateOf("")

    //是否添加账号
    var addAccount = false

    //每3秒检查一次WebView Cookie是否设置完成
    fun checkWebViewCookieValue(activity: Activity) {
//        accountLogin = true
//        viewModelScope.launch {
//            val cookieManager = CookieManager.getInstance()
//            do {
//                delay(3000)
//                val cookieMap = stringToCookieMap(cookieManager.getCookie(WEB_URL.split("?").first()))
//                if (cookieMap.isNotEmpty()) {
//                    viewModelScope.launch {
//                        AccountManager.checkCookieAvailable(cookieMap, onSuccess = {
//                            setAccount(activity,cookieMap, it)
//                        })
//                        cookieManager.removeAllCookies {}
//                        cookieManager.removeSessionCookies {}
//                        "获取完成".show()
//                        accountLogin = false
//                    }
//                }
//            } while (accountLogin)
//        }
    }

    //检查输入的cookie
    fun checkInputCookieValue(activity: Activity) {
//        if (cookieInput.isBlank()) {
//            "输入Cookie以继续".show()
//            return
//        }
//        val cookieMap = stringToCookieMap(cookieInput)
//        if (cookieMap.isEmpty()) {
//            "Cookie错误".show()
//            return
//        }
//        viewModelScope.launch {
//            AccountManager.checkCookieAvailable(cookieMap, onSuccess = {
//                setAccount(activity,cookieMap, it)
//            }, onFailed = {
//                it.show()
//            })
//        }
    }

    //将字符串转成header
    private fun stringToCookieMap(input: String): Map<String, String> {
        val cookieMap = mutableMapOf<String, String>()
        input.split(";").toList()
            .forEach {
                val kv = it.split("=")
                val key = kv.first().trim()
                val value = kv.last().trim()
                cookieMap[key] = value
            }
        val ltuid = cookieMap[CookieHelper.Keys.LTuid]
        val ltoken = cookieMap[CookieHelper.Keys.LToken]
        val account_id = cookieMap[CookieHelper.Keys.AccountID]
        val cookie_token = cookieMap[CookieHelper.Keys.CookieToken]
        return if (!(ltuid.isNullOrBlank() || ltoken.isNullOrBlank() || account_id.isNullOrBlank() || cookie_token.isNullOrBlank())) {
            mapOf(
                "ltuid" to ltuid,
                "ltoken" to ltoken,
                "account_id" to account_id,
                "cookie_token" to cookie_token)
        } else {
            mapOf()
        }
    }

    //设置账户数据
    private fun setAccount(
        activity: Activity,
        cookieMap: Map<String, String>,
    ) {
        viewModelScope.launch {
            val accountDataList = mutableListOf<AccountData>()
//                val accountData = AccountData(
//                    nickName = gameRole.nickname,
//                    loginUid = cookieMap[Cookie.Keys.AccountID] ?: "",
//                    region = gameRole.region,
//                    regionName = gameRole.region_name,
//                    gameUid = gameRole.game_uid,
//                    gameLevel = gameRole.level,
//                    cookieMap = cookieMap
//                )
//                when (index) {
//                    0 -> {
//                        //添加账号
//                        if (addAccount) {
//                            accountDataList += accountData
//                        } else {
//                            AccountManager.setMainAccount(accountData)
//                        }
//                    }
//                    else -> {
//                        accountDataList += accountData
//                    }
//                }
//            AccountManager.addToAccountList(accountDataList)

            if(addAccount){
                activity.finish()
                "账号添加完成"
            }else{
                activity.startActivity(Intent(activity,HomeScreen::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
                "账号设置完成"
            }.show()

        }
    }
}