package com.lianyi.paimonsnotebook.ui.activity

import android.content.Intent
import android.os.Bundle
import com.lianyi.paimonsnotebook.bean.GetGameRolesByCookieBean
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.databinding.ActivityUserLoginBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.ActivityRequestCode
import com.lianyi.paimonsnotebook.lib.information.ActivityResponseCode
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class SetCookieActivity : BaseActivity() {
    lateinit var bind :ActivityUserLoginBinding

    companion object{
        var isAddUser = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)


        bind.setCookie.setOnClickListener {
            checkCookie()
        }

        bind.goLogin.setOnClickListener {
            val intent = Intent(this,HoYoLabLoginActivity::class.java)
            HoYoLabLoginActivity.isAddUser = isAddUser
            startActivityForResult(intent,ActivityRequestCode.LOGIN)
        }
    }


    private fun checkCookie(){
        if(bind.cookieInput.text.isNullOrEmpty()){
            "你还没有输入cookie".show()
            return
        }

        val cookieMap = mutableMapOf<String,String>()
        bind.cookieInput.text.toString().split(";").forEach { split->
            val map = split.split("=")
            cookieMap += map.first() to map.last()
        }

        val account_id = cookieMap[Constants.LTUID_NAME]?:cookieMap[Constants.ACCOUNT_ID_NAME]?:""
        val ltoken = cookieMap[Constants.LTOKEN_NAME]?:cookieMap["lToken"]?:""
        val cookie_token = cookieMap[Constants.COOKIE_TOKEN_NAME]?:""

        if(account_id.isNotEmpty()&&ltoken.isNotEmpty()&&cookie_token.isNotEmpty()){
            val cookie = "ltuid=${account_id};ltoken=${ltoken};"
            Ok.getGameRolesByCookie(cookie){
                runOnUiThread {
                    if(it.ok){
                        val cookie2 = "account_id=${account_id};cookie_token=${cookie_token}"
                        Ok.getGameRolesByCookie(cookie2){
                            runOnUiThread {
                                if(it.ok){
                                    val roles = GSON.fromJson(it.optString("data"),
                                        GetGameRolesByCookieBean::class.java)

                                    if(roles.list.size>0){
                                        val user = UserBean(
                                            roles.list.first().nickname,
                                            account_id,
                                            roles.list.first().region,
                                            roles.list.first().region_name,
                                            roles.list.first().game_uid,
                                            ltoken,
                                            cookie_token,
                                            roles.list.first().level
                                        )
                                        if(isAddUser){
                                            val userList = mutableListOf<UserBean>()
                                            JSONArray(usp.getString(JsonCacheName.USER_LIST,"[]")).toList(userList)
                                            userList+=user

                                            usp.edit().apply {
                                                putString(JsonCacheName.USER_LIST, GSON.toJson(userList))
                                                apply()
                                            }
                                        }else{
                                            usp.edit().apply{
                                                mainUser = user
                                                putString(JsonCacheName.MAIN_USER_NAME,GSON.toJson(user))
                                                apply()
                                            }
                                        }
                                        setResult(ActivityResponseCode.OK)
                                        finish()
                                        "Cookie设置成功"
                                    }else{
                                        "该账号没有原神角色信息"
                                    }
                                }else{
                                    "错误的Cookie"
                                }.show()
                            }
                        }
                    }else{
                        "错误的Cookie".show()
                    }
                }
            }
        }else{
           "错误的Cookie".show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==ActivityResponseCode.OK){
            when(requestCode){
                ActivityRequestCode.LOGIN->{
                    setResult(ActivityResponseCode.OK)
                    finish()
                }
            }
        }
    }

    override fun onDestroy() {
        isAddUser = false
        super.onDestroy()
    }

}