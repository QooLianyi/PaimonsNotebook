package com.lianyi.paimonsnotebook.common.util.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

object NotificationHelper {

    enum class Type {
        Normal,
        HighImportance,
        Progress
    }

    private const val NormalChannelId = "100"
    private const val HighImportanceNormalChannelId = "200"
//    private const val DailyNoteChannelId = "1"
//    private const val TravellerNoteChannelId = "2"

    const val NormalNotificationId = 10000
    const val HighNotificationId = 20000
    const val ProgressNotificationId = 10001
    const val NotificationId = 10002
    const val LargeTextNotificationId = 10003
    const val CustomNotificationId = 10004


    private val mContext by lazy {
        PaimonsNotebookApplication.context
    }

    private val mManager by lazy {
        (mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                listOf(
                    getNotificationChannel(
                        NormalChannelId,
                        "PaimonsNotebook",
                        NotificationManager.IMPORTANCE_DEFAULT,
                        "派蒙笔记本默认通知频道",
                        false),
                    getNotificationChannel(
                        HighImportanceNormalChannelId,
                        "PaimonsNotebook",
                        NotificationManager.IMPORTANCE_HIGH,
                        "派蒙笔记本重要通知频道",
                        true),
                ).forEach {
                    createNotificationChannel(it)
                }
            }
        }
    }


    fun buildNormalNotification(
        title: String,
        content: String,
        type: Type = Type.Normal,
        intent: Intent = Intent(),
        autoCancel: Boolean = false,
    ): Int {

        val pendingIntent =
            PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getChannelIdByType(type)

        val build = NotificationCompat.Builder(mContext, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.icon_klee_square)
            .setAutoCancel(autoCancel)
            .setContentIntent(pendingIntent)
            .build()

        mManager.notify(NormalNotificationId, build)

        return NormalNotificationId
    }

    fun buildHighImportanceNotification(
        title: String,
        content: String,
        type: Type = Type.HighImportance,
        intent: Intent = Intent(),
        autoCancel: Boolean = false,
    ): Int {

        val pendingIntent =
            PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = getChannelIdByType(type)

        val build = NotificationCompat.Builder(mContext, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.icon_klee_square)
            .setAutoCancel(autoCancel)
            .setContentIntent(pendingIntent)
            .setNumber(1)// 自定义桌面通知数量
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)// 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
            .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)// 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
            .build()

        mManager.notify(NormalNotificationId, build)

        return NormalNotificationId
    }

    fun buildProgressNotification(
        title: String,
        content: String,
        max: Int = 100,
        progress: Int = 0,
        type: Type = Type.Progress,
        indeterminate: Boolean = false,
    ):Pair<Int,NotificationCompat.Builder> {
        val channelId = getChannelIdByType(type)

        val builder = NotificationCompat.Builder(mContext, channelId)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(R.drawable.icon_klee_square)
            .setProgress(max, progress,indeterminate)

        mManager.notify(ProgressNotificationId, builder.build())

        return ProgressNotificationId to builder
    }

    fun buildLargeTextNotification(
        title: String,
        content: String,
        type: Type = Type.Normal,
        intent: Intent =Intent(),
        autoCancel: Boolean = false,
    ):Pair<Int,NotificationCompat.Builder> {
        val channelId = getChannelIdByType(type)

        val pendingIntent =
            PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(mContext, channelId)
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setSmallIcon(R.drawable.icon_klee_square)
            .setLargeIcon(BitmapFactory.decodeResource(mContext.resources,R.drawable.icon_klee_square))
            .setAutoCancel(autoCancel)
            .setContentIntent(pendingIntent)

        mManager.notify(LargeTextNotificationId, builder.build())

        return LargeTextNotificationId to builder
    }

    fun sendNotify(notificationId: Int, builder: NotificationCompat.Builder) {
        mManager.notify(notificationId, builder.build())
    }

    fun cancelNotificationByNotificationId(notificationId: Int) {
        mManager.cancel(notificationId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNotificationChannel(
        channelId: String,
        channelName: String,
        importance: Int,
        description: String,
        showBadge: Boolean,
    ) =
        NotificationChannel(
            channelId,
            channelName,
            importance
        ).apply {
            this.description = description
            setShowBadge(showBadge)
        }

    private fun getChannelIdByType(type: Type) =
        when (type) {
//            Type.DailyNote -> DailyNoteChannelId
//            Type.TravellerNote -> TravellerNoteChannelId
            Type.HighImportance -> HighImportanceNormalChannelId
            else -> NormalChannelId
        }


}