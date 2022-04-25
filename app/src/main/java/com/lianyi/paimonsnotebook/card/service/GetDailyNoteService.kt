package com.lianyi.paimonsnotebook.card.service

import android.app.*
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.lianyi.paimonsnotebook.card.CardRequest
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.card.CardUtil
import com.lianyi.paimonsnotebook.card.CardUtil.cShowLong
import com.lianyi.paimonsnotebook.card.appwidget.CardDailyNoteOverviewWidget
import com.lianyi.paimonsnotebook.card.appwidget.CardResinType1Widget
import com.lianyi.paimonsnotebook.card.appwidget.CardResinType2Widget
import kotlinx.coroutines.Job
import org.json.JSONObject

class GetDailyNoteService:Service() {
    override fun onBind(p0: Intent?): IBinder?  = null
    private lateinit var cardType:String
    private lateinit var action:String
    private lateinit var timeOutJob:Job

    private lateinit var context: Context

    companion object{
        //每次请求需要间隔10秒
        private const val SP_CACHE_TIME = "daily_note_get_time"
        private const val MIN_CACHE_TIME = 10000
        private val requestQueue = mutableListOf<Intent>()
        private var isWorking = false
    }

    override fun onCreate() {
        super.onCreate()
        println("GetDailyNoteService Start")

        context = baseContext
        CardUtil.context = context
        CardRequest.context = context
        timeOutJob = CardUtil.setServiceTimeOut(this)
        sendNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        requestQueue.clear()
        isWorking = false
        println("GetDailyNoteService End")
    }

    private fun getDailyNote(){
        CardUtil.checkStatus({
            //请求时间如果小于10秒钟直接发送更新广播
            val cacheTime = CardUtil.sp.getLong(SP_CACHE_TIME, 0L)
            if (System.currentTimeMillis() - cacheTime >= MIN_CACHE_TIME) {
                CardRequest.getDailyNote {
                    setDailyNotebook(it)
                }
            } else {
                notifyUpdate()
            }
        }, {
            it.cShowLong()
            stopSelf()
        })
    }

    private fun setDailyNotebook(it:JSONObject){
        if(it.optString("retcode")=="0"){
            CardUtil.setDailyNote(CardUtil.mainUser.gameUid,it.optString("data"))
            CardUtil.setValue(SP_CACHE_TIME, System.currentTimeMillis())
            notifyUpdate()
        }else{
            Handler(Looper.getMainLooper()).post {
                "获取每日便笺失败:${it.optString("message")}".cShowLong()
                stopSelf()
            }
        }
    }

    //发送广播 通知更新并关闭服务
    private fun notifyUpdate(){
        sendBroadcast(Intent(
            when(action){
                CardUtil.CLICK_ACTION->CardUtil.CLICK_UPDATE_ACTION
                else->CardUtil.UPDATE_ACTION
            }
        ).apply {
            component = ComponentName(
                context,
                when(cardType){
                    CardUtil.TYPE_RESIN_TYPE1->CardResinType1Widget::class.java
                    CardUtil.TYPE_RESIN_TYPE2->CardResinType2Widget::class.java
                    CardUtil.TYPE_DAILY_NOTE_OVERVIEW->CardDailyNoteOverviewWidget::class.java
                    else->CardResinType1Widget::class.java
                })
        })
        checkRequestQueue()
    }

    //检查队列内是否还有请求
    private fun checkRequestQueue(){
        requestQueue.removeIf {
            val iType = it.getStringExtra("type")
            val iAction = it.getStringExtra("action")
            iType==cardType&&iAction==action
        }
        if(requestQueue.isNotEmpty()){
            cardType = requestQueue.first().getStringExtra("type")?:CardUtil.TYPE_RESIN_TYPE1
            action = requestQueue.first().getStringExtra("action")?:CardUtil.UPDATE_ACTION
            getDailyNote()
        }else{
            timeOutJob.cancel()
            stopSelf()
        }
    }

    private fun sendNotification(){
        val notice = Notification.Builder(context).apply {
            setContentTitle("正在获取每日便笺")
            setContentText("获取完成后自动关闭")
            .setSmallIcon(R.drawable.icon_klee)
        }
        val channelId = "getDailyNoteChannelId"
        val channelName = "getDailyNoteChannelName"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                createNotificationChannel(
                    NotificationChannel(
                        channelId,
                        channelName,
                        NotificationManager.IMPORTANCE_HIGH
                    )
                )
            }
            notice.setChannelId(channelId)
        }
        startForeground(233,notice.build())
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        cardType = intent?.getStringExtra("type")?:CardUtil.TYPE_RESIN_TYPE1
        action = intent?.getStringExtra("action")?:CardUtil.UPDATE_ACTION
        requestQueue += intent!!

        if(!isWorking){
           getDailyNote()
            isWorking = true
        }

        return START_STICKY
    }
}