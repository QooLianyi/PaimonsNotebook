package com.lianyi.paimonsnotebook.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.lianyi.paimonsnotebook.R

class NotificationManager {
    companion object{
        private const val COMMON_CHANNEL_ID = "100"
        private const val COMMON_CHANNEL_NAME = "PAIMON"


        val manager by lazy{
            PaiMonsNoteBook.context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        fun sendNotification(title:String,content:String){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                val channel = NotificationChannel(COMMON_CHANNEL_ID,COMMON_CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
                manager.createNotificationChannel(channel)
            }

            val notification = NotificationCompat.Builder(PaiMonsNoteBook.context,COMMON_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getBroadcast(PaiMonsNoteBook.context,0, Intent(),0))
                .setSmallIcon(R.drawable.icon_klee)
                .setAutoCancel(true)
                .build()
            manager.notify(1, notification)

        }
    }
}