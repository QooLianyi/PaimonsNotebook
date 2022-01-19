package com.lianyi.paimonsnotebook.util

import com.google.gson.Gson
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.lib.information.ResponseCode
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Ok {
    companion object{
        val GSON by lazy {
            Gson()
        }

        //通用请求
        private val client by lazy {
            OkHttpClient.Builder().addInterceptor {
                val request = it.request().newBuilder()
                val cookie = "ltuid=${mainUser?.loginUid};ltoken=${mainUser?.lToken};account_id=${mainUser?.loginUid};cookie_token=${mainUser?.cookieToken}"
                request.addHeader("DS", getDS("role_id=${mainUser?.gameUid}&server=${mainUser?.region}"))
                request.addHeader("Cookie",cookie)
                request.addHeader("x-rpc-client_type","5")
                request.addHeader("x-rpc-app_version",Constants.APP_VERSION)
                it.proceed(request.build())
            }.build()
        }

        private val clientC by lazy{
            OkHttpClient()
        }

        //通用请求
        fun get(url:String,block: (JSONObject) -> Unit){
            val request = Request.Builder().get().url(url).build()
            client.newCall(request).enqueue(MyCallBack(block))
        }

        //每日签到
        fun dailySign(user: UserBean, block: (JSONObject) -> Unit){
            val toRequestBody = """
                {
                    "act_id":"e202009291139501",
                    "region":"${user.region}",
                    "uid":"${user.gameUid}"
                }
            """.trimIndent().toMyRequestBody()
            val request = Request.Builder().url(MiHoYoApi.DAILY_SIGN).post(toRequestBody)
                .addHeader("DS", getSignDS(Constants.SIGN_SALT))
                .addHeader("x-rpc-app_version",Constants.SIGN_APP_VERSION)
                .addHeader("x-rpc-client_type","5")
                .addHeader("x-rpc-device_id","c27b1ee7-54c2-3eaf-bfb1-8a2c4dd64939")
                .addHeader("Cookie","account_id=${user.loginUid};cookie_token=${user.cookieToken};")
                .build()
            block(JSONObject(clientC.newCall(request).execute().body?.string()?:""))
        }

        fun get(url:String,cookie:String,block: (JSONObject) -> Unit){
            val request = Request.Builder()
                .get()
                .url(url)
                .addHeader("Cookie",cookie)
                .build()
            clientC.newCall(request).enqueue(MyCallBack(block))
        }

        fun get(url:String,user: UserBean,block: (JSONObject) -> Unit){
            val request = Request.Builder()
                .get()
                .url(url)
                .addHeader("Cookie",MiHoYoApi.getCookie(user))
                .build()
            clientC.newCall(request).enqueue(MyCallBack(block))
        }

        //获取抽卡记录请求 (同步)
        fun getGachaHistory(url: String,block: (JSONObject) -> Unit){
            val request = Request.Builder().get().url(url).build()
            block(JSONObject(client.newCall(request).execute().body?.string()?:""))
        }

        fun getCharacter(cookie: String,body:String,requestBody: RequestBody,block: (JSONObject) -> Unit){
            val request = Request.Builder()
                .post(requestBody)
                .url(MiHoYoApi.GET_CHARACTER_LIST_DETAIL)
                .addHeader("x-rpc-app_version",Constants.APP_VERSION)
                .addHeader("DS", getDS("",body))
                .addHeader("x-rpc-client_type","5")
                .addHeader("Cookie",cookie)
                .build()
            clientC.newCall(request).enqueue(MyCallBack(block))
        }

        fun getAbyss(cookie: String,query:String,block: (JSONObject) -> Unit){
            val request = Request.Builder()
                .get()
                .url(MiHoYoApi.getAbyssUrl(mainUser!!.gameUid, mainUser!!.region))
                .addHeader("x-rpc-app_version",Constants.APP_VERSION)
                .addHeader("DS", getDS(query))
                .addHeader("x-rpc-client_type","5")
                .addHeader("Cookie",cookie)
                .build()
            clientC.newCall(request).enqueue(MyCallBack(block))
        }

    }
}

class MyCallBack(val block:(JSONObject)->Unit): Callback{
    override fun onFailure(call: Call, e: IOException) {
    }

    override fun onResponse(call: Call, response: Response) {
        try {
            block(JSONObject(response.body!!.string()))
        }catch (e:Exception){
            block(JSONObject("""
                {
                    "data": null,
                    "message": "can not convert to json",
                    "retcode": ${ResponseCode.CAN_NOT_CONVERT_TO_JSON}
                }
            """.trimIndent()))
        }
    }
}

val GSON:Gson
get() = Ok.GSON

val JSONObject.ok
get() = this.optString("retcode") == ResponseCode.OK

inline fun <reified T> JSONObject.toList(name:String, list:MutableList<T>){
    val arr = this.getJSONArray(name)
    repeat(arr.length()){
        list += GSON.fromJson(arr[it].toString(),T::class.java)
    }
}

inline fun <reified T> JSONArray.toList(list: MutableList<T>){
    repeat(this.length()){
        list += GSON.fromJson(this[it].toString(),T::class.java)
    }
}

