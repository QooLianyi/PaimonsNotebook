package com.lianyi.paimonsnotebook.card.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
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
import kotlinx.coroutines.Job
import org.json.JSONObject

class GetMonthLedgerService:Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    lateinit var action:String
    lateinit var cardType:String
    lateinit var timeOutJob: Job

    private lateinit var context: Context

    companion object{
        private const val SP_CACHE_TIME = "month_ledger_get_time"
        private const val MIN_CACHE_TIME = 10000
        private val requestQueue = mutableListOf<Intent>()
        private var isWorking = false
        var requestOk = false
    }

    override fun onCreate() {
        super.onCreate()
        println("GetMonthLedgerService Start")

        context = baseContext
        CardUtil.context = context
        CardRequest.context = context
        timeOutJob = CardUtil.setServiceTimeOut(this)
        sendNotification()
    }

    override fun onDestroy() {
        super.onDestroy()
        isWorking = false
        println("GetMonthLedgerService End")
    }

    private fun getMonthLedger(){
        CardUtil.checkStatus({
            val cacheTime = CardUtil.sp.getLong(SP_CACHE_TIME, 0L)
            if (System.currentTimeMillis() - cacheTime >= MIN_CACHE_TIME) {
                CardRequest.getMonthLedger {
                    setMonthLedger(it)
                }
            } else {
                notifyUpdate()
            }
        }, {
            it.cShowLong()
            stopSelf()
        })
    }

    private fun setMonthLedger(it:JSONObject){
        if(it.optString("retcode")=="0"){
            CardUtil.setMonthLedgerCache(CardUtil.mainUser.gameUid,it.optString("data"))
            CardUtil.setValue(SP_CACHE_TIME, System.currentTimeMillis())
            requestOk = true
            notifyUpdate()
        }else{
            Handler(Looper.getMainLooper()).post {
                "获取旅行者札记失败:${it.optString("message")}".cShowLong()
                stopSelf()
            }
        }
    }

    private fun notifyUpdate(){
        sendBroadcast(Intent(
            when(action){
                CardUtil.CLICK_ACTION->CardUtil.CLICK_UPDATE_ACTION
                else->CardUtil.UPDATE_ACTION
            }
        ).apply {
            component = ComponentName(context,
                when(cardType){
                    CardUtil.TYPE_DAILY_NOTE_OVERVIEW-> CardDailyNoteOverviewWidget::class.java
                    else-> CardDailyNoteOverviewWidget::class.java
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
            getMonthLedger()
        }else{
            timeOutJob.cancel()
            requestOk = false
            stopSelf()
        }
    }

    private fun sendNotification(){
        val notice = Notification.Builder(context).apply {
            setContentTitle("正在获取旅行者札记")
            setContentText("获取完成后自动关闭")
                .setSmallIcon(R.drawable.icon_klee)
        }
        val channelId = "getMonthLedgerChannelId"
        val channelName = "getMonthLedgerChannelName"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
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
        startForeground(234,notice.build())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        action = intent?.getStringExtra("action")?:CardUtil.UPDATE_ACTION
        cardType = intent?.getStringExtra("type")?:CardUtil.TYPE_RESIN_TYPE1
        requestQueue+=intent!!

        if(!isWorking){
            getMonthLedger()
            isWorking = true
        }

        return START_STICKY
    }
}