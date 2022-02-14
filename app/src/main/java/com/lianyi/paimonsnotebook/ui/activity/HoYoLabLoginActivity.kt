package com.lianyi.paimonsnotebook.ui.activity

import android.os.Bundle
import android.webkit.*
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.GetGameRolesByCookieBean
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.databinding.ActivityHoYoLabLoginBinding
import com.lianyi.paimonsnotebook.lib.information.ActivityResponseCode
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import kotlin.concurrent.thread
class HoYoLabLoginActivity : BaseActivity() {
    lateinit var bind:ActivityHoYoLabLoginBinding

    companion object{
        var isAddUser = false
    }

    private val cookieMap = mutableMapOf<String,String>()

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

            loadUrl(MiHoYoApi.LOGIN)
            webViewClient = object :WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    val cookieManager = CookieManager.getInstance()
                    val cookies = cookieManager.getCookie(url)

                    //当网页加载完毕时
                    if(view?.progress==100&&cookies!=null&&cookies.isNotEmpty()){
                        val cookieSplit = cookies.split(";")
                        var isLogin = false

                        cookieSplit.forEach {
                            val cookie = it.trim().split("=")
                            cookieMap += cookie.first().trim() to cookie.last().trim()
                            when(cookie.first()){
                                Constants.ACCOUNT_ID_NAME-> isLogin = true
                            }
                        }

                        //当登录完成时
                        if(isLogin && isAddUser){
                            val cookie = "ltuid=${cookieMap[Constants.LTUID_NAME]?:""};ltoken=${cookieMap[Constants.LTOKEN_NAME]?:""};account_id=${cookieMap[Constants.ACCOUNT_ID_NAME]?:""};cookie_token=${cookieMap[Constants.COOKIE_TOKEN_NAME]?:""}"
                            val userList = mutableListOf<UserBean>()
                            JSONArray(usp.getString(JsonCacheName.USER_LIST,"[]")).toList(userList)

                            getGameRolesByCookie(cookie){
                                if(it!=null){
                                    userList += it
                                }else{

                                }
                                usp.edit().apply {
                                    putString(JsonCacheName.USER_LIST,GSON.toJson(userList))
                                    apply()
                                }
                                clearCookie()
                            }
                        }else if(isLogin){
                            mainUser?.lToken = cookieMap[Constants.LTOKEN_NAME]?:""
                            mainUser?.loginUid = cookieMap[Constants.LTUID_NAME]?:""
                            mainUser?.cookieToken = cookieMap[Constants.COOKIE_TOKEN_NAME]?:""

                            Ok.get(MiHoYoApi.GET_GAME_ROLES_BY_COOKIE){
                                if(it.ok){
                                    val roles = GSON.fromJson(it.optString("data"),
                                        GetGameRolesByCookieBean::class.java)

                                    with(roles.list.first()){
                                        mainUser?.gameLevel = level
                                        mainUser?.nickName = nickname
                                        mainUser?.region = region
                                        mainUser?.regionName = region_name
                                        mainUser?.gameUid = game_uid
                                    }

                                    usp.edit().apply{
                                        putString(JsonCacheName.MAIN_USER_NAME,GSON.toJson(mainUser))
                                        apply()
                                    }

                                    //请理Cookie
                                    clearCookie()
                                }
                            }
                        }
                    }
                }
            }
        }
        setContentMargin(bind.root)
    }

    private fun getGameRolesByCookie(cookie:String,block:(UserBean?)->Unit){
        Ok.getGameRolesByCookie(cookie){
            if(it.ok){
                val roles = GSON.fromJson(it.optString("data"),
                    GetGameRolesByCookieBean::class.java)
                if(roles.list.size>0){
                    block(UserBean(
                        roles.list.first().nickname,
                        cookieMap[Constants.LTUID_NAME]?:"",
                        roles.list.first().region,
                        roles.list.first().region_name,
                        roles.list.first().game_uid,
                        cookieMap[Constants.LTOKEN_NAME]?:"",
                        cookieMap[Constants.COOKIE_TOKEN_NAME]?:"",
                        roles.list.first().level
                    ))
                }
            }else{
                block(null)
            }
        }
    }


    private fun clearCookie(){
        runOnUiThread {
            CookieManager.getInstance().removeAllCookies {
                if(it){
                    CookieManager.getInstance().flush()
                    setResult(ActivityResponseCode.OK)
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isAddUser = false
    }

}