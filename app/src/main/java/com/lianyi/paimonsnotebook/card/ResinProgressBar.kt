package com.lianyi.paimonsnotebook.card

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.work.*
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.card.service.ForegroundTest
import com.lianyi.paimonsnotebook.card.service.GetDailyNoteService
import com.lianyi.paimonsnotebook.card.service.GetMonthLedgerService
import com.lianyi.paimonsnotebook.card.service.GoSummerLandService
import com.lianyi.paimonsnotebook.card.worker.ResinWorker
import com.lianyi.paimonsnotebook.util.sp
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ResinProgressBar : AppWidgetProvider() {

    private val UPDATE_ACTION = "com.lianyi.widget.UPDATE_ACTION"
    private val SUMMER_LAND = "com.lianyi.widget.SUMMER_LAND"
    private val GET_DAILY_NOTE ="com.lianyi.widget.DAILY_NOTE"

    private lateinit var remoteViews: RemoteViews

    companion object{
        var isPaimon = false
        //存放小组件id
        private val idsSet = mutableSetOf<Int>()
        val WORKER_NAME = "work_name"
        var isEnabled = true

        lateinit var mContext: Context
        val sp by lazy {
            mContext.getSharedPreferences("cache_info",Context.MODE_PRIVATE)
        }

        fun updateAppWidgets(context: Context){
            println("进入更新界面")
            val appWidgetManager = AppWidgetManager.getInstance(context)
            idsSet.forEach {
                val view = RemoteViews(context.packageName,R.layout.card_resin_progress_bar).apply {
                    val text = context.getSharedPreferences("cache_info",Context.MODE_PRIVATE).getString("rand","-100")
                    println("rand = ${text}")
                    setTextViewText(R.id.appwidget_text,text)
                    setOnClickPendingIntent(R.id.go_home, PendingIntent.getService(context,0,
                        Intent(context,GoSummerLandService::class.java),0))
                }
                appWidgetManager.updateAppWidget(it,view)
            }
        }

        fun notifyDataChange(context: Context){
            println("notifyDataChange:通知更新")
            updateAppWidgets(context)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        mContext = context
        sp.edit().apply {
            putInt("update_count",sp.getInt("update_count",0)+1)
            apply()
        }
        println("update count = ${sp.getInt("update_count",-100)}")
        remoteViews = RemoteViews(context.packageName,R.layout.card_resin_progress_bar)
        remoteViews.setOnClickPendingIntent(
            R.id.go_home,
            registerGoSummerLandAction(context)
        )
        remoteViews.setOnClickPendingIntent(
            R.id.start_service,
            registerDailyNotebook(context)
        )

        remoteViews.setImageViewResource(
            R.id.img,
            if(isPaimon){
                R.drawable.icon_klee
            }else{
                R.drawable.icon_paimon
            }
        )
        appWidgetIds.forEach {
            appWidgetManager.updateAppWidget(it,remoteViews)
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        println("action = ${intent?.action}  isPaimon = ${isPaimon}")
        when(intent?.action){
            UPDATE_ACTION->{
                isPaimon = !isPaimon
                imageViewToggle(context!!)
            }
            SUMMER_LAND->{
                context?.startService(Intent(context,GetMonthLedgerService::class.java))
            }
            GET_DAILY_NOTE -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context?.startForegroundService(Intent(context, GetDailyNoteService::class.java))
                } else {
                    context?.startService(Intent(context, GetDailyNoteService::class.java))
                }
            }
        }
    }

    private fun imageViewToggle(context: Context){
        updateRemoteViewImage(R.id.img,context,if(isPaimon){
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                context.startForegroundService(Intent(context,ForegroundTest::class.java))
            }else{
                context.startService(Intent(context,ForegroundTest::class.java))
            }
            R.drawable.icon_paimon
        }else{
            context.stopService(Intent(context,ForegroundTest::class.java))
            R.drawable.icon_klee
        })
    }

    private fun registerDailyNotebook(context: Context):PendingIntent{
        val intent = Intent(SUMMER_LAND).apply {
            component = ComponentName(context,ResinProgressBar::class.java)
        }
        return PendingIntent.getBroadcast(context,233,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun registerGoSummerLandAction(context: Context):PendingIntent{
        val intent = Intent(UPDATE_ACTION).apply {
            component = ComponentName(
                context,
                ResinProgressBar::class.java
            )
        }
        return PendingIntent.getBroadcast(context,R.id.go_home,intent,PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun updateRemoteViewImage(id:Int,context: Context,resource:Int){
        if (!this::remoteViews.isInitialized){
            remoteViews = RemoteViews(context.packageName,R.layout.card_resin_progress_bar)
        }
        remoteViews.setImageViewResource(id,resource)

        val componentName =ComponentName(context,ResinProgressBar::class.java)
        AppWidgetManager.getInstance(context).updateAppWidget(componentName,remoteViews)
    }

    private fun setUpWorker(context:Context){
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED) //当有可用网络时执行
            .setRequiresDeviceIdle(true) //设备空闲时执行
            .build()

        val repeatRequest = PeriodicWorkRequestBuilder<ResinWorker>(15,TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(WORKER_NAME,ExistingPeriodicWorkPolicy.KEEP,repeatRequest)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        setUpWorker(context)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        appWidgetIds?.forEach {
            idsSet.remove(it)
            WorkManager.getInstance(context!!).cancelUniqueWork(ResinWorker.getWorkName(it))
        }
        super.onDeleted(context, appWidgetIds)
    }

    override fun onDisabled(context: Context) {
        idsSet.forEach {
            WorkManager.getInstance(context).cancelUniqueWork(WORKER_NAME)
        }
    }

}



