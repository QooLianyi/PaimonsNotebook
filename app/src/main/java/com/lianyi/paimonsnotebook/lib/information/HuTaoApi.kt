package com.lianyi.paimonsnotebook.lib.information

import com.lianyi.paimonsnotebook.util.MyCallBack
import com.lianyi.paimonsnotebook.util.Ok
import com.lianyi.paimonsnotebook.util.sp
import com.lianyi.paimonsnotebook.util.toMyRequestBody
import okhttp3.*
import org.json.JSONObject

class HuTaoApi {
    companion object{

        private val client by lazy {
            OkHttpClient.Builder().authenticator { route, response ->
                response.request.newBuilder().header("Authorization", "Bearer ${sp.getString(SP_TOKEN,"")}").build()
            }.build()
        }

        fun get(url: String,block: (JSONObject) -> Unit){
            val request = Request.Builder()
                .url(url)
                .build()
            client.newCall(request).enqueue(MyCallBack(block))
        }

        fun post(url:String,requestBody: RequestBody,block: (JSONObject) -> Unit){
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()
            client.newCall(request).enqueue(MyCallBack(block))
        }

        fun login(block: (JSONObject) -> Unit){
            val request = Request.Builder()
                .url(LOGIN)
                .post(LOGIN_REQUEST_BODY)
                .build()
            client.newCall(request).enqueue(MyCallBack(block))
        }

        private const val BASE_URL = "https://hutao-api.snapgenshin.com"
        //登录
        const val LOGIN = "${BASE_URL}/Auth/Login"
        //总览数据
        const val OVER_VIEW = "${BASE_URL}/Statistics/Overview"
        //角色出场数据
        const val AVATAR_PARTICIPATION = "${BASE_URL}/Statistics/AvatarParticipation"
        //圣遗物数据
        const val AVATAR_RELIQUARY_USAGE = "${BASE_URL}/Statistics/AvatarReliquaryUsage"
        //组队数据
        const val TEAM_COLLOCATION = "${BASE_URL}/Statistics/TeamCollocation"
        //武器使用数据
        const val WEAPON_USAGE = "${BASE_URL}/Statistics/WeaponUsage"
        //角色命座数据
        const val CONSTELLATION = "${BASE_URL}/Statistics/Constellation"
        //队伍使用数据
        const val TEAM_COMBINATION = "${BASE_URL}/Statistics/TeamCombination"

        //获得圣遗物数据
        const val GET_RELIQUARIES = "${BASE_URL}/GenshinItems/Reliquaries"
        //获得武器数据
        const val GET_WEAPONS  = "${BASE_URL}/GenshinItems/Weapons"
        //获得角色数据
        const val GET_AVATARS = "${BASE_URL}/GenshinItems/Avatars"

        const val APP_ID = "08d9e7ea-7b27-45c1-8110-bd996f053d90"
        const val SECRET = "xlcuZ5xS+ER5SHo3LLEfOstNrKxTJfMPWZwIfdXvNPk="

        //上传数据前上传一次genshin_item
        const val POST_GENSHIN_ITEM = "${BASE_URL}/GenshinItems/Upload"

        //请求的TOKEN sp
        const val SP_TOKEN = "hutao_token"

        const val RECORD_UPLOAD = "${BASE_URL}/Record/Upload"

        val LOGIN_REQUEST_BODY by lazy {
             """
                {
                  "appId": "$APP_ID",
                  "secret": "$SECRET"
                }
            """.trimIndent().toMyRequestBody()
        }
    }
}