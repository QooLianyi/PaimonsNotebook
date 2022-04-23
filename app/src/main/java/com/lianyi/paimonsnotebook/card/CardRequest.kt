package com.lianyi.paimonsnotebook.card

import android.app.Service
import android.content.Context
import com.google.gson.Gson
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.card.CardUtil.mainUser
import com.lianyi.paimonsnotebook.util.Ds
import com.lianyi.paimonsnotebook.util.MyCallBack
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.*
import java.util.concurrent.TimeUnit

object CardRequest {
    lateinit var context: Context

    private const val APP_VERSION = "2.14.1"

    private val ds by lazy {
        Ds()
    }

    private val client by lazy {
        OkHttpClient.Builder().addInterceptor {
            val request = it.request().newBuilder()
            request.addHeader("DS", ds.getDS("role_id=${mainUser.gameUid}&server=${mainUser.region}"))
            request.addHeader("Cookie", getCookie(mainUser))
            request.addHeader("x-rpc-client_type","5")
            request.addHeader("x-rpc-app_version", APP_VERSION)
            it.proceed(request.build())
        }.retryOnConnectionFailure(true)
            .readTimeout(3L, TimeUnit.MINUTES)
            .build()
    }

    fun getMonthLedger(block:(JSONObject)->Unit){
        val month = Calendar.getInstance().get(Calendar.MONTH)
        client.newCall(
            Request.Builder().url(getMonthLedgerUrl(month, mainUser.gameUid, mainUser.region)).build()
        ).enqueue(MyCallBack{
            block(it)
        })
    }

    fun getDailyNote(block:(JSONObject)->Unit){
        client.newCall(
            Request.Builder().url(getDailyNoteUrl(mainUser.gameUid, mainUser.region)).build()
        ).enqueue(MyCallBack{
            block(it)
        })
    }

    fun getMonthLedgerUrl(month:Int, gameUID: String, server: String):String{
        return "https://hk4e-api.mihoyo.com/event/ys_ledger/monthInfo?month=$month&bind_uid=$gameUID&bind_region=$server&bbs_presentation_style=fullscreen&bbs_auth+required=true&mys_source=GameRecord"
    }

    private fun getDailyNoteUrl(gameUID:String,server:String):String{
        return "https://api-takumi-record.mihoyo.com/game_record/app/genshin/api/dailyNote?role_id=$gameUID&server=$server"
    }

    private fun getCookie(user: UserBean):String{
        return "ltuid=${user.loginUid};ltoken=${user.lToken};account_id=${user.loginUid};cookie_token=${user.cookieToken}"
    }

}