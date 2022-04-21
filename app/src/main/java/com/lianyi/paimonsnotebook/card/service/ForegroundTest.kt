package com.lianyi.paimonsnotebook.card.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import com.lianyi.paimonsnotebook.R

class ForegroundTest:Service() {

    private val channelId = "paimonsnootebook_channel_id_01"
    private val channelName = "paimonsnootebook_channel_name_01"

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        getNotificationBuilder().apply {
            setContentTitle("派蒙笔记本")
            setContentText("前台服务测试")
            setSmallIcon(R.drawable.icon_klee)
        }.build().also {
            startForeground(2,it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    private fun getNotificationBuilder():Notification.Builder{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
                createNotificationChannel(
                    NotificationChannel(channelId,channelName, NotificationManager.IMPORTANCE_HIGH)
                )
            }
            Notification.Builder(this,channelId)
        }else{
            Notification.Builder(this)
        }
    }
}