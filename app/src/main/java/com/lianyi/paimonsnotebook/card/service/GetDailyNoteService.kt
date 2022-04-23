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
import org.json.JSONObject

class GetDailyNoteService:Service() {
    override fun onBind(p0: Intent?): IBinder?  = null
    lateinit var cardType:String
    lateinit var action:String

    private lateinit var context: Context

    companion object{
        //每次请求需要间隔10秒
        private const val SP_CACHE_TIME = "daily_note_get_time"
        private const val MIN_CACHE_TIME = 10000
        val requestQueue = mutableListOf<Intent>()
    }

    override fun onCreate() {
        super.onCreate()
        println("GetDailyNoteService Start")
        context = baseContext
        CardRequest.context = context
        sendNotification()
        CardUtil.checkMainUser({
            //请求时间如果小于10秒钟直接发送更新广播
            val cacheTime = CardUtil.sp.getLong(SP_CACHE_TIME,0L)
            if(System.currentTimeMillis()-cacheTime>= MIN_CACHE_TIME){
                CardRequest.getDailyNote {
                    setDailyNotebook(it)
                }
            }else{
                notifyUpdate()
            }
        },{
            "没有默认用户,请登录后再次尝试更新AppWidget".cShowLong()
            stopSelf()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        requestQueue.removeIf {
            val iType = it.getStringExtra("type")
            val iAction = it.getStringExtra("action")
            iType==cardType&&iAction==action
        }
        if(requestQueue.isNotEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(requestQueue.first())
            } else {
                context.startService(requestQueue.first())
            }
        }
        println("GetDailyNoteService End")
    }

    private fun setDailyNotebook(it:JSONObject){
        if(it.optString("retcode")=="0"){
            CardUtil.setDailyNote(CardUtil.mainUser.gameUid,it.optString("data"))
            notifyUpdate()
            stopSelf()
        }else{
            Handler(Looper.getMainLooper()).post {
                "请求每日便笺失败:${it.optString("message")}".cShowLong()
                stopSelf()
            }
        }
    }

    private fun notifyUpdate(){
        //发送广播 通知更新
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
                    CardUtil.TYPE_DAILY_NOTE_OVERVIEW->CardDailyNoteOverviewWidget::class.java
                    else->CardResinType1Widget::class.java
                })
        })
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
        return START_STICKY
    }
}