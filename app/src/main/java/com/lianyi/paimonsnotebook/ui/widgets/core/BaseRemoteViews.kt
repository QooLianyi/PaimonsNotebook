package com.lianyi.paimonsnotebook.ui.widgets.core

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.TypedValue
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
import com.lianyi.paimonsnotebook.common.util.convert.TypeUnitConvert
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setImageTint
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setTextColor
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.enums.AppWidgetBackgroundScaleType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

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

    private fun dpToPx(value: Number) = TypeUnitConvert.dpToPx(value,resources.displayMetrics).toInt()

    private fun spToPx(value: Number) = TypeUnitConvert.spToPx(value,resources.displayMetrics)
        /*
        *
        * TypedValue.convertDimensionToPixels(
        TypedValue.COMPLEX_UNIT_SP,
        value.toFloat(),
        resources.displayMetrics
    )*/

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
    suspend fun setCommonStyle(
        configuration: AppWidgetConfiguration,
        textIds: IntArray = intArrayOf(),
        tintImageIds: IntArray = intArrayOf()
    ) {
        setBackgroundStyle(configuration)

        setTextColor(configuration, textIds)

        setImageTintColor(configuration, tintImageIds)

    }

    private suspend fun setBackgroundStyle(configuration: AppWidgetConfiguration) {
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

        val backgroundImage = configuration.background?.backgroundImageUrl

        //如果自定义背景图片不为空的话就显示图片
        if (backgroundImage?.isEmpty() == false) {
            loadImage(backgroundImage)

            val imageFile =
                PaimonsNotebookImageLoader.getCacheImageFileByUrl(backgroundImage)

            val imageFileBitmap = BitmapFactory.decodeFile(imageFile?.path)

            val widthScale = bitmap.width.toFloat() / imageFileBitmap.width
            val heightScale = bitmap.height.toFloat() / imageFileBitmap.height

            val scaleType = configuration.background.backgroundScaleType
                ?: AppWidgetBackgroundScaleType.CropCenter

            val scale = getImageScaleByScaleType(
                scaleType,
                widthScale,
                heightScale
            )

            val imageWidth = (scale * imageFileBitmap.width).roundToInt()
            val imageHeight = (scale * imageFileBitmap.height).roundToInt()

            val imageBitmap =
                Bitmap.createScaledBitmap(imageFileBitmap, imageWidth, imageHeight, false)

            val paint = Paint().apply {
                isAntiAlias = true
            }

            //设置偏移量,实现居中的效果
            val offsetX = (bitmap.width - imageWidth) / 2
            val offsetY = (bitmap.height - imageHeight) / 2

            println("offsetX = ${offsetX}")
            println("offsetY = ${offsetY}")

            println("image = ${imageBitmap.width} ${imageBitmap.height}")
            println("widget = ${bitmap.width} ${bitmap.height}")

            val rect =
                Rect(
                    offsetX.coerceAtLeast(0),
                    offsetY.coerceAtLeast(0),
                    //当某个轴小于零时,将被强制设置为0,同时尺寸要乘以2倍来适配组件的尺寸,也可以直接设置为组件的宽高
                    (imageBitmap.width + offsetX).coerceAtMost(bitmap.width),
                    (imageBitmap.height + offsetY).coerceAtMost(bitmap.height)
                )
            val rectF = RectF(rect)

            //绘制一个圆角矩形
            canvas.drawRoundRect(rectF, radius, radius, paint)
            //只保留重叠的部分
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

            canvas.drawBitmap(imageBitmap, offsetX.toFloat(), offsetY.toFloat(), paint)

            imageFileBitmap.recycle()
            imageBitmap.recycle()
        } else {
            GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadii = radii
                setSize(width, height)
                setBounds(0, 0, intrinsicWidth, intrinsicHeight)

                setColor(backgroundColor)

                draw(canvas)
            }
        }

        val textPaint = Paint().apply {
            color = Color.RED
            textSize = spToPx(30)
            textAlign = Paint.Align.LEFT
            isStrikeThruText = true
            isUnderlineText = true

            typeface = Typeface.create(Typeface.DEFAULT,Typeface.BOLD)
        }

        canvas.drawText("id:${appWidgetId}", bitmap.width / 2f, bitmap.height / 2f, textPaint)

        setImageViewBitmap(R.id.background, bitmap)
        bitmap.recycle()
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

    private fun getImageScaleByScaleType(
        type: AppWidgetBackgroundScaleType,
        widthScale: Float,
        heightScale: Float
    ) = when (type) {
        AppWidgetBackgroundScaleType.FitCenter -> min(widthScale, heightScale)
        AppWidgetBackgroundScaleType.CropCenter -> max(widthScale, heightScale)
        else -> 1f
    }

    open suspend fun onUpdateContent(intent: Intent?): RemoteViews? = null

    open suspend fun setDailyNoteWidget(dailyNoteWidgetData: DailyNoteWidgetData) {}

    open suspend fun setDailyNote(dailyNoteData: DailyNoteData) {}

    open suspend fun setGachaRecord() {}

    open suspend fun setAbyss() {}
}