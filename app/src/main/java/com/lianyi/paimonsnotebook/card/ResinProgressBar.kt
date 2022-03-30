package com.lianyi.paimonsnotebook.card

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.content.edit
import androidx.work.*
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.card.worker.ResinWorker
import com.lianyi.paimonsnotebook.util.sp
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ResinProgressBar : AppWidgetProvider() {

    //定义更新action
    private val UPDATE_ACTION = "com.lianyi.widget.UPDATE_ACTION"

    companion object{
        var mIndex = 0
        //存放小组件id
        private val idsSet = mutableSetOf<Int>()
        val WORKER_NAME = "work_name"

        fun updateAppWidgets(context: Context){
            println("进入更新界面")
            val appWidgetManager = AppWidgetManager.getInstance(context)
            idsSet.forEach {
                val view = RemoteViews(context.packageName,R.layout.card_resin_progress_bar).apply {
                    val text = context.getSharedPreferences("cache_info",Context.MODE_PRIVATE).getString("rand","-100")
                    println("rand = ${text}")
                    setTextViewText(R.id.appwidget_text,text)
                }
                appWidgetManager.updateAppWidget(it,view)
            }
        }

        fun notifyDataChange(context: Context){
            println("notifyDataChange:通知更新")
            updateAppWidgets(context)
        }
    }

    //接收小组件点击时发送的广播
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
//        if(intent?.action == UPDATE_ACTION){
//            //更新
//            updateAllViews(context!!, AppWidgetManager.getInstance(context))
//        }else if(intent?.hasCategory(Intent.CATEGORY_ALTERNATIVE)==true){
//            mIndex = 0
//            //更新
//            updateAllViews(context!!, AppWidgetManager.getInstance(context))
//        }
//        println("接收广播+${intent?.action} isUpdate = ${intent?.action==UPDATE_ACTION} setSize = ${idsSet.size}")
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        println("update")
        notifyDataChange(context)

        appWidgetIds.forEach {
            idsSet += it
        }
//        val request = OneTimeWorkRequestBuilder<ResinWorker>()
//            .build()
//        WorkManager.getInstance(context).enqueueUniqueWork(WORKER_NAME,ExistingWorkPolicy.REPLACE,request)
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
}



