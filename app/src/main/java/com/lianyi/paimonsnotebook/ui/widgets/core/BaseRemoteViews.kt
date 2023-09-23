package com.lianyi.paimonsnotebook.ui.widgets.core

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import coil.request.ImageRequest
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
* 小组件远端视图基类
* */
open class BaseRemoteViews(
    protected val appWidgetId: Int,
    protected val targetCls: Class<out BaseAppWidget>,
    layout: Int
) : RemoteViews(PaimonsNotebookApplication.context.packageName, layout) {

    private val diskCacheDao by lazy {
        PaimonsNotebookDatabase.database.diskCacheDao
    }

    val context by lazy {
        PaimonsNotebookApplication.context
    }

    protected fun basePendingIntent(
        action: String,
        unionIdentity: String = "",
        bundle: Bundle? = null
    ): PendingIntent {
        val intent = Intent(action).apply {
            component = ComponentName(context, targetCls)
            data = AppWidgetHelper.getUnionData(appWidgetId, unionIdentity = unionIdentity)
            putExtra(AppWidgetHelper.PARAM_APPWIDGET_ID, appWidgetId)
            if (bundle != null) {
                putExtras(bundle)
            }
        }
        return PendingIntent.getBroadcast(
            context,
            AppWidgetHelper.APPWIDGET_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    protected val updatePendingIntent: PendingIntent
        get() = basePendingIntent(AppWidgetHelper.ACTION_UPDATE_WIDGET)

    protected val goBindPendingIntent: PendingIntent
        get() = basePendingIntent(AppWidgetHelper.ACTION_GO_CONFIGURATION)

    protected val goValidatePendingIntent: PendingIntent
        get() = basePendingIntent(AppWidgetHelper.ACTION_GO_VALIDATE)

    //加载网络图片至本地
    protected suspend fun loadImage(
        vararg urls: String, diskCacheName: String = "", diskCache: DiskCache = DiskCache(
            url = "",
            name = diskCacheName,
            createFrom = "桌面组件",
            type = DiskCacheDataType.Stable,
            lastUseFrom = "桌面组件"
        )
    ) {
        withContext(Dispatchers.IO) {
            val jobs = urls.filter {
                PaimonsNotebookImageLoader.getCacheImageFileByUrl(it) == null
            }.map {
                launch {
                    PaimonsNotebookImageLoader.current.execute(
                        ImageRequest.Builder(PaimonsNotebookApplication.context)
                            .data(it)
                            .build()
                    )
                    diskCacheDao.insert(
                        diskCache.copy(url = it)
                    )
                }
            }
            jobs.joinAll()
        }
    }


    open suspend fun onUpdateContent(intent: Intent?): RemoteViews? = null

    open suspend fun setDailyNoteWidget(dailyNoteWidgetData: DailyNoteWidgetData) {}

    open suspend fun setDailyNote(dailyNoteData: DailyNoteData) {}

    open suspend fun setGachaRecord() {}

    open suspend fun setAbyss() {
    }
}