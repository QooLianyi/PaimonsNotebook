package com.lianyi.paimonsnotebook.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
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

        fun sendFileExportPathNotification(){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                val channel = NotificationChannel(COMMON_CHANNEL_ID,COMMON_CHANNEL_NAME,NotificationManager.IMPORTANCE_HIGH)
                manager.createNotificationChannel(channel)
            }

            val path = "${PaiMonsNoteBook.context.getExternalFilesDir(null)?.absolutePath}"
            val uri = Uri.parse("content://com.android.externalstorage.documents/document/primary:$path")

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.type = "*/*"
//            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI,uri)
            intent.setDataAndType(uri,"file/*")

            val pendIntent = PendingIntent.getActivity(PaiMonsNoteBook.context,0,intent,0)

            val notification = NotificationCompat.Builder(PaiMonsNoteBook.context,COMMON_CHANNEL_ID)
                .setContentTitle("导出到以下路径:")
                .setContentText(path)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getBroadcast(PaiMonsNoteBook.context,0, Intent(),0))
                .setSmallIcon(R.drawable.icon_klee)
                .setAutoCancel(true)
                .setContentIntent(pendIntent)
                .build()
            manager.notify(1, notification)
        }

    }
}