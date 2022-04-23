package com.lianyi.paimonsnotebook.card.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import com.lianyi.paimonsnotebook.card.CardRequest
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.card.CardUtil
import com.lianyi.paimonsnotebook.card.CardUtil.cShowLong
import com.lianyi.paimonsnotebook.card.appwidget.CardDailyNoteOverviewWidget
import org.json.JSONObject

class GetMonthLedgerService:Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    lateinit var action:String
    lateinit var cardType:String

    private lateinit var context: Context

    override fun onCreate() {
        super.onCreate()
        context = baseContext
        CardRequest.context = context
        sendNotification()
        println("GetMonthLedgerService Start")
        CardUtil.checkMainUser({
            CardRequest.getMonthLedger {
                setMonthLedger(it)
            }
        },{
            "没有默认用户,请登录后再次尝试更新AppWidget".cShowLong()
            stopSelf()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        println("GetMonthLedgerService End")
    }

    private fun setMonthLedger(it:JSONObject){
        if(it.optString("retcode")=="0"){
            CardUtil.setMonthLedgerCache(CardUtil.mainUser.gameUid,it.optString("data"))
            sendBroadcast(Intent(
                when(action){
                    CardUtil.CLICK_ACTION->CardUtil.CLICK_UPDATE_ACTION
                    else->CardUtil.UPDATE_ACTION
                }
            ).apply {
                component = ComponentName(context,
                    when(cardType){
                    CardUtil.TYPE_DAILY_NOTE_OVERVIEW->CardDailyNoteOverviewWidget::class.java
                    else-> CardDailyNoteOverviewWidget::class.java
                })
            })
            stopSelf()
        }else{
            Handler(Looper.getMainLooper()).post {
                "获取旅行者札记失败:${it.optString("message")}".cShowLong()
            }
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
        return START_STICKY
    }
}