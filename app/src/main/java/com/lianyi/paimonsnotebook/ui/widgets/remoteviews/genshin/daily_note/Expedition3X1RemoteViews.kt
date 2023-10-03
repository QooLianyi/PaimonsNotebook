package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note

import android.graphics.BitmapFactory
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.util.image.PaimonsNotebookImageLoader
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.ErrorRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon3X1

/*
* 小组件派遣委托3*1
* */
internal class Expedition3X1RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon3X1::class.java,
    R.layout.widget_layout_linear_horizontal_empty
) {
    init {
        setOnClickPendingIntent(R.id.container, updatePendingIntent)
    }

    override suspend fun setDailyNote(dailyNoteData: DailyNoteData) {
        removeAllViews(R.id.linear_layout)

        if (dailyNoteData.expeditions.isEmpty()) {
            val views = ErrorRemoteViews(
                appWidgetBinding.appWidgetId,
                AppWidgetCommon3X1::class.java,
                "没有派遣委托哦",
                R.drawable.emotion_icon_nahida_drink
            )
            addView(R.id.linear_layout, views)
            return
        }

        loadImage(
            urls = dailyNoteData.expeditions.map { it.avatar_side_icon }.toTypedArray(),
            diskCacheName = "派遣角色头像"
        )

        val textIds = mutableListOf<Int>()

        dailyNoteData.expeditions.forEach { expedition ->
            val itemView =
                RemoteViews(context.packageName, R.layout.widget_layout_expedition_item).apply {
                    val imageFile =
                        PaimonsNotebookImageLoader.getCacheImageFileByUrl(expedition.avatar_side_icon)
                    val bitmap = BitmapFactory.decodeFile(imageFile?.path)
                    setImageViewBitmap(R.id.avatar, bitmap)

                    val second = expedition.remained_time.toLongOrNull() ?: 1L

                    val timeText = if (second != 0L) {
                        val hour = second / 3600

                        if (hour > 0) {
                            "${hour}小时"
                        } else {
                            "${second % 60}分"
                        }
                    } else {
                        "已完成"
                    }

                    setTextViewText(
                        R.id.text,
                        timeText
                    )

                    textIds += R.id.text
                }
            addView(R.id.linear_layout, itemView)
        }

        setCommonStyle(appWidgetBinding.configuration,textIds.toIntArray())
    }
}