package com.lianyi.paimonsnotebook.ui.activity

import android.os.Bundle
import android.util.Log
import android.webkit.*
import com.lianyi.paimonsnotebook.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.GetGameRolesByCookieBean
import com.lianyi.paimonsnotebook.config.ActivityResponseCode
import com.lianyi.paimonsnotebook.config.Settings
import com.lianyi.paimonsnotebook.config.URL
import com.lianyi.paimonsnotebook.databinding.ActivityHoYoLabLoginBinding
import com.lianyi.paimonsnotebook.util.*

class HoYoLabLoginActivity : BaseActivity() {
    lateinit var bind:ActivityHoYoLabLoginBinding

    companion object{
        var isAddUser = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityHoYoLabLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)

        with(bind.web){
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.domStorageEnabled = true
            settings.blockNetworkImage = false
            settings.useWideViewPort = true

            loadUrl(URL.LOGIN)
            webViewClient = object :WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    val cookieManager = CookieManager.getInstance()
                    val cookies = cookieManager.getCookie(url)

                    //当网页加载完毕时
                    if(view?.progress==100&&cookies!=null&&cookies.isNotEmpty()){
                        val cookieMap = mutableMapOf<String,String>()
                        val cookieSplit = cookies.split(";")
                        var isLogin = false
                        cookieSplit.forEach {
                            val cookie = it.trim().split("=")
                            cookieMap += cookie.first().trim() to cookie.last().trim()
                            println("first --> ${cookie.first()} last --> ${cookie.last()}")
                            when(cookie.first()){
                                Settings.ACCOUNT_ID_NAME-> isLogin = true
                            }
                        }

                        mainUser?.lToken = cookieMap[Settings.LTOKEN_NAME]?:""
                        mainUser?.loginUid = cookieMap[Settings.LTUID_NAME]?:""
                        mainUser?.cookieToken = cookieMap[Settings.COOKIE_TOKEN_NAME]?:""

                        //当登录完成时
                        if(isLogin){
                            Ok.get(URL.GET_GAME_ROLES_BY_COOKIE){
                                if(it.ok){
                                    val roles = GSON.fromJson(it.optString("data"),
                                        GetGameRolesByCookieBean::class.java)
                                    if(isAddUser){

                                    }else{
                                        with(roles.list.first()){
                                            mainUser?.gameLevel = level
                                            mainUser?.nickName = nickname
                                            mainUser?.region = region
                                            mainUser?.regionName = region_name
                                            mainUser?.gameUid = game_uid
                                        }
                                    }

                                    val edit = usp.edit()
                                    edit.putString(Settings.USP_MAIN_USER_NAME,GSON.toJson(mainUser))
                                    edit.apply()

                                    //请理Cookie
                                    runOnUiThread {
                                        cookieManager.removeAllCookies {
                                            if(it){
                                                cookieManager.flush()
                                                setResult(ActivityResponseCode.OK)
                                                finish()
                                            }
                                        }
                                    }
                                }else{
                                    println(it.toString())
                                    Log.e("Lian::", "onPageFinished: 获取角色出错拉")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}