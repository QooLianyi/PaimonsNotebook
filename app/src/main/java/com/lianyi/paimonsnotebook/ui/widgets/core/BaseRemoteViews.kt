package com.lianyi.paimonsnotebook.ui.widgets.core

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import coil.imageLoader
import coil.request.ImageRequest
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.data.AppWidgetConfiguration
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setImageTint
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setTextColor
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
* 小组件远端视图基类
*
* appWidgetId:桌面组件id
* targetCls:绑定的标准组件
* layout:布局资源id
* dpHeight:布局高度(dp),未指定时使用全部可用空间来显示背景
* */
open class BaseRemoteViews(
    protected val appWidgetId: Int,
    protected val targetCls: Class<out BaseAppWidget>,
    layout: Int,
    private val dpHeight: Float = -1f
) : RemoteViews(PaimonsNotebookApplication.context.packageName, layout) {

    private val diskCacheDao by lazy {
        PaimonsNotebookDatabase.database.diskCacheDao
    }

    protected val context by lazy {
        PaimonsNotebookApplication.context
    }

    private val manager by lazy {
        AppWidgetManager.getInstance(context)
    }

    private val resources by lazy {
        Resources.getSystem()
    }

    //屏幕方向是否是垂直
    private val isPortrait: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    //桌面组件宽度
    protected val width: Int
        get() {
            val extra = if (isPortrait) AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH
            else AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH
            return dpToPx(manager.getAppWidgetOptions(appWidgetId).getInt(extra, 1))
        }

    //桌面组件高度
    protected val height: Int
        get() {
            val extra = if (isPortrait) AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT
            else AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT
            return dpToPx(manager.getAppWidgetOptions(appWidgetId).getInt(extra, 1))
        }

    private fun dpToPx(value: Number) = (value.toFloat() * resources.displayMetrics.density).toInt()

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

    protected fun getValidatePendingIntent(mid: String) =
        basePendingIntent(AppWidgetHelper.ACTION_GO_VALIDATE, bundle = Bundle().apply {
            putString("mid", mid)
        })

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
                    PaimonsNotebookApplication.context.applicationContext.imageLoader.execute(
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

    /*
    * 设置通用样式,如背景样式
    *
    * configurationScreen:配置类
    * textIds:字体id
    * tintImageIds:设置前景色的图片id
    * */
    fun setCommonStyle(
        configuration: AppWidgetConfiguration,
        textIds: IntArray = intArrayOf(),
        tintImageIds: IntArray = intArrayOf()
    ) {
        setBackgroundStyle(configuration)

        setTextColor(configuration, textIds)

        setImageTintColor(configuration, tintImageIds)
    }

    private fun setBackgroundStyle(configuration: AppWidgetConfiguration) {
        val radius = dpToPx((configuration.background?.backgroundRadius ?: 8f).toInt()).toFloat()
        val backgroundColor = configuration.background?.backgroundColor ?: White.toArgb()

        val radii = floatArrayOf(
            radius, radius,
            radius, radius,
            radius, radius,
            radius, radius
        )

        val bitmap = Bitmap.createBitmap(
            width,
            if (dpHeight == -1f) height
            else dpToPx(dpHeight),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadii = radii

            setColor(backgroundColor)
            setSize(width, height)
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)

            draw(canvas)
        }

        setImageViewBitmap(R.id.background, bitmap)
    }

    private fun setTextColor(configuration: AppWidgetConfiguration, textIds: IntArray) {
        textIds.forEach {
            setTextColor(it, configuration.textColor)
        }
    }

    private fun setImageTintColor(configuration: AppWidgetConfiguration, tintImageIds: IntArray) {
        val tintColor = configuration.imageTintColor
        tintImageIds.forEach {
            setImageTint(it, tintColor)
        }
    }

    open suspend fun onUpdateContent(intent: Intent?): RemoteViews? = null

    open suspend fun setDailyNoteWidget(dailyNoteWidgetData: DailyNoteWidgetData) {}

    open suspend fun setDailyNote(dailyNoteData: DailyNoteData) {}

    open suspend fun setGachaRecord() {}

    open suspend fun setAbyss() {}
}