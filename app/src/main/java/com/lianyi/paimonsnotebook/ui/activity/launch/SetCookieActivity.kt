package com.lianyi.paimonsnotebook.ui.activity.launch

import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import com.lianyi.paimonsnotebook.bean.GetGameRolesByCookieBean
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.card.CardUtil
import com.lianyi.paimonsnotebook.databinding.ActivityUserLoginBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.ActivityRequestCode
import com.lianyi.paimonsnotebook.lib.information.ActivityResponseCode
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray

class SetCookieActivity : BaseActivity() {
    lateinit var bind: ActivityUserLoginBinding

    companion object {
        var isAddUser = false

        fun checkCookie(
            account_id: String,
            ltoken: String,
            cookie_token: String,
            block: (Boolean,GetGameRolesByCookieBean?) -> Unit
        ) {
            val cookie = "ltuid=${account_id};ltoken=${ltoken};"
            Ok.getGameRolesByCookie(cookie) {
                if (it.ok) {
                    val cookie2 = "account_id=${account_id};cookie_token=${cookie_token}"
                    Ok.getGameRolesByCookie(cookie2) {
                        if (it.ok) {
                            val roles = GSON.fromJson(
                                it.optString("data"),
                                GetGameRolesByCookieBean::class.java
                            )
                            block(true,roles)
                        } else {
                            block(false,null)
                        }
                    }
                }else{
                    block(false,null)
                }
            }
        }

        fun refreshMainUserInformation(){
            usp.edit().apply {
                putString(
                    JsonCacheName.MAIN_USER_NAME,
                    GSON.toJson(mainUser)
                )
                apply()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivityUserLoginBinding.inflate(layoutInflater)
        setContentView(bind.root)


        bind.setCookie.setOnClickListener {
            check()
        }

        bind.goLogin.setOnClickListener {
            val intent = Intent(this, HoYoLabLoginActivity::class.java)
            HoYoLabLoginActivity.isAddUser = isAddUser
            startActivityForResult(intent, ActivityRequestCode.LOGIN)
        }
    }


    private fun check() {
        if (bind.cookieInput.text.isNullOrEmpty()) {
            "你还没有输入cookie".show()
            return
        }

        val cookieMap = mutableMapOf<String, String>()
        bind.cookieInput.text.toString().filter { !it.isWhitespace()}.split(";").forEach { split ->
            val map = split.split("=")
            cookieMap += map.first() to map.last()
        }

        val account_id =
            cookieMap[Constants.LTUID_NAME] ?: cookieMap[Constants.ACCOUNT_ID_NAME] ?: ""
        val ltoken = cookieMap[Constants.LTOKEN_NAME] ?: cookieMap["lToken"] ?: ""
        val cookie_token = cookieMap[Constants.COOKIE_TOKEN_NAME] ?: ""

        if (account_id.isNotEmpty() && ltoken.isNotEmpty() && cookie_token.isNotEmpty()) {
            checkCookie(account_id,ltoken,cookie_token){ b: Boolean, roles: GetGameRolesByCookieBean? ->
                runOnUiThread {
                    if (b) {
                        roles!!
                        if (roles.list.size > 0) {
                            val getUserList = mutableListOf<UserBean>()
                            roles.list.forEach { gameRole->
                                getUserList += UserBean(
                                    gameRole.nickname,
                                    account_id,
                                    gameRole.region,
                                    gameRole.region_name,
                                    gameRole.game_uid,
                                    ltoken,
                                    cookie_token,
                                    gameRole.level
                                )
                            }
                            if (isAddUser) {
                                val userList = mutableListOf<UserBean>()
                                JSONArray(
                                    usp.getString(
                                        JsonCacheName.USER_LIST,
                                        "[]"
                                    )
                                ).toList(userList)
                                getUserList.forEach {
                                    userList.add(0,it)
                                }
                                usp.edit().apply {
                                    putString(
                                        JsonCacheName.USER_LIST,
                                        GSON.toJson(userList)
                                    )
                                    apply()
                                }
                            } else {
                                usp.edit().apply {
                                    mainUser = getUserList.first()
                                    putString(
                                        JsonCacheName.MAIN_USER_NAME,
                                        GSON.toJson(getUserList.first())
                                    )
                                    apply()
                                }
                                sp.edit {
                                    putBoolean("main_user_change",true)
                                    apply()
                                }
                                if(roles.list.size>1){
                                    val userList = mutableListOf<UserBean>()
                                    JSONArray(
                                        usp.getString(
                                            JsonCacheName.USER_LIST,
                                            "[]"
                                        )
                                    ).toList(userList)
                                    userList += getUserList.last()
                                    usp.edit().apply {
                                        putString(
                                            JsonCacheName.USER_LIST,
                                            GSON.toJson(userList)
                                        )
                                        apply()
                                    }
                                }
                            }
                            setResult(ActivityResponseCode.OK)
                            finish()
                            "Cookie设置成功"
                        } else {
                            "该账号没有原神角色信息"
                        }
                    } else {
                        "错误的Cookie:验证失败"
                    }.show()
                }
            }
        } else {
            "错误的Cookie:缺少所需的参数(account_id/ltuid,lToken,cookie_token)".showLong()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == ActivityResponseCode.OK) {
            when (requestCode) {
                ActivityRequestCode.LOGIN -> {
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