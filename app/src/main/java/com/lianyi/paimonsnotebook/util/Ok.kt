package com.lianyi.paimonsnotebook.util

import com.google.gson.Gson
import com.lianyi.paimonsnotebook.config.ResponseCode
import com.lianyi.paimonsnotebook.config.Settings
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Ok {
    companion object{
        val GSON by lazy {
            Gson()
        }
        val client by lazy {
            OkHttpClient.Builder().addInterceptor {
                val request = it.request().newBuilder()
                val cookie = "ltuid=${mainUser?.loginUid};ltoken=${mainUser?.lToken};account_id=${mainUser?.loginUid};cookie_token=${mainUser?.cookieToken}"
                request.addHeader("DS", getDS("role_id=${mainUser?.gameUid}&server=${mainUser?.region}"))
                request.addHeader("Cookie",cookie)
                request.addHeader("x-rpc-client_type","5")
                request.addHeader("x-rpc-app_version",Settings.APP_VERSION)
                println("cookie = $cookie")
                it.proceed(request.build())
            }.build()
        }

        fun get(url:String,block: (JSONObject) -> Unit){
            val request = Request.Builder().get().url(url).build()
            client.newCall(request).enqueue(MyCallBack(block))
        }

    }
}

class MyCallBack(val block:(JSONObject)->Unit): Callback{
    override fun onFailure(call: Call, e: IOException) {
    }

    override fun onResponse(call: Call, response: Response) {
        block(JSONObject(response.body!!.string()))
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

