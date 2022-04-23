package com.lianyi.paimonsnotebook.card

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import com.google.gson.Gson
import com.lianyi.paimonsnotebook.bean.MonthLedgerBean
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.bean.dailynote.DailyNoteBean
import java.lang.StringBuilder

object CardUtil {
    lateinit var context:Context

    const val TYPE_RESIN_TYPE1 = "card_resin_type_1"
    const val TYPE_DAILY_NOTE_OVERVIEW = "card_daily_note_overview"

    const val APPWIDGET_UPDATE = "android.appwidget.action.APPWIDGET_UPDATE"
    //更新action
    const val UPDATE_ACTION = "com.lianyi.widget.UPDATE_ACTION"
    //点击action
    const val CLICK_ACTION = "com.lianyi.widget.CLICK_ACTION"
    //接收点击更新action
    const val CLICK_UPDATE_ACTION = "com.lianyi.widget.CLICK_UPDATE_ACTION"

    private const val USP_NAME = "user_info"
    private const val MAIN_USER_NAME = "main_user"

    val mainUser: UserBean by lazy {
        Gson().fromJson(
            context.getSharedPreferences(USP_NAME, Context.MODE_PRIVATE).getString(
                MAIN_USER_NAME,"{}")!!, UserBean::class.java)
    }

    val gson by lazy {
        Gson()
    }

    val sp: SharedPreferences by lazy {
        context.getSharedPreferences("cache_info", Context.MODE_PRIVATE)
    }

    fun String.cShow(){
        Toast.makeText(context,this, Toast.LENGTH_SHORT).show()
    }

    fun String.cShowLong(){
        Toast.makeText(context,this, Toast.LENGTH_LONG).show()
    }

    fun getCacheDailyNoteBean(uid:String): DailyNoteBean = Gson().fromJson(
        sp.getString("daily_note_cache_${uid}",""), DailyNoteBean::class.java)

    fun getMonthLedgerBean(uid: String): MonthLedgerBean? = gson.fromJson(
        sp.getString("month_ledger_cache_${uid}",""),
        MonthLedgerBean::class.java
    )

    fun setMonthLedgerCache(uid:String,json:String){
        sp.edit().apply {
            putString("month_ledger_cache_${uid}",json)
            apply()
        }
    }

    fun setDailyNote(uid:String,json: String){
        sp.edit().apply {
            putString("daily_note_cache_${uid}",json)
            apply()
        }
    }

    fun formatTime(recoverTime:Long):String{
        return if(recoverTime>3600){
            "${recoverTime/3600}小时${recoverTime%3600/60}分钟"
        }else{
            "${recoverTime%3600/60}分钟"
        }
    }

    fun getRecoverTime(recoverTime:Long):String{
        val sb = StringBuilder()
        if(recoverTime/3600<10){
            sb.append("0")
        }
        if(recoverTime>3600){
            sb.append("${recoverTime/3600}:")
        }else{
            sb.append("0:")
        }
        val minute = recoverTime%60
        if(minute==0L){
            sb.append("00")
        }else if(minute<10){
            sb.append(0)
        }else{
            sb.append(recoverTime%60)
        }

        return sb.toString()
    }

    fun getQualityConvertTime(dailyNoteBean: DailyNoteBean):String{
        val qualityConvertRecoverTimeTextStringBuffer = StringBuffer()
        with(dailyNoteBean.transformer.recovery_time){
            if(day>0)
                qualityConvertRecoverTimeTextStringBuffer.append("${day}天")
            if(hour>0)
                qualityConvertRecoverTimeTextStringBuffer.append("${hour}小时")
            if(minute>0)
                qualityConvertRecoverTimeTextStringBuffer.append("${day}分钟")
            if(second>0)
                qualityConvertRecoverTimeTextStringBuffer.append("${day}秒")
            if(qualityConvertRecoverTimeTextStringBuffer.isNotEmpty()){
                qualityConvertRecoverTimeTextStringBuffer.append("后可再次使用")
            }else{
                qualityConvertRecoverTimeTextStringBuffer.append("准备完成")
            }
        }
        return qualityConvertRecoverTimeTextStringBuffer.toString()
    }

    fun checkMainUser(ok:()->Unit,fail:()->Unit){
        if(mainUser.isNull()){
            fail()
        }else{
            ok()
        }
    }
}