package com.lianyi.paimonsnotebook.card.worker

import android.appwidget.AppWidgetManager
import android.content.Context
import android.widget.RemoteViews
import androidx.core.content.contentValuesOf
import androidx.core.content.edit
import androidx.work.*
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.card.ResinProgressBar
import com.lianyi.paimonsnotebook.util.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ResinWorker(private val appContext: Context, params: WorkerParameters) : CoroutineWorker(
    appContext,
    params
) {
    private val job = Job()
    private val coroutineScope =CoroutineScope(Dispatchers.Main + job)

    override suspend fun doWork(): Result {
//        val widgetId = inputData.getInt(INPUT_DATA_WIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID)
//        require(widgetId!=AppWidgetManager.INVALID_APPWIDGET_ID)
//        updateWidget(applicationContext,AppWidgetManager.getInstance(applicationContext), arrayOf(widgetId).toIntArray())
        coroutineScope.launch {
            val rand = (0..10000).random()
            println("随机摇出一个数字!:${rand}")
            appContext.getSharedPreferences("cache_info",Context.MODE_PRIVATE).edit {
                putString("rand","$rand")
                apply()
            }
            ResinProgressBar.notifyDataChange(appContext)
        }
        return Result.success()
    }

    companion object{
        private const val INPUT_DATA_WIDGET_ID = "com.lianyi.appwidget.resin_card1"

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetIds: IntArray?
        ) {
            val remoteViews = RemoteViews(context.packageName, R.id.card_view).apply {
                setTextViewText(R.id.appwidget_text,System.currentTimeMillis().toString())
            }
            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
        }

        fun updateWidget(context: Context,widgetId:Int){
            val data = Data.Builder()
                .putInt(INPUT_DATA_WIDGET_ID,widgetId)
                .build()
            val request = OneTimeWorkRequestBuilder<ResinWorker>()
                .setInputData(data)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(getWorkName(widgetId),
                ExistingWorkPolicy.REPLACE,request)
        }

        fun getWorkName(widgetId:Int):String = "widget_update_${widgetId}"
    }
}


