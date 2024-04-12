package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget

import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsContentHelper
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon2X1

class DailyNoteWidget2X1RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon2X1::class.java,
    R.layout.widget_layout_daily_note_widget_2_1
) {

    init {
        setOnClickPendingIntent(R.id.container, updatePendingIntent)
    }

    override suspend fun setDailyNoteWidget(dailyNoteWidgetData: DailyNoteWidgetData) {
        setTextViewText(
            R.id.resin_text,
            "${dailyNoteWidgetData.current_resin}/${dailyNoteWidgetData.max_resin}"
        )
        setTextViewText(
            R.id.daily_task_text,
            RemoteViewsContentHelper.getDailyTaskContentByState(dailyNoteWidgetData)
        )
        setTextViewText(
            R.id.home_coin_text,
            "${dailyNoteWidgetData.current_home_coin}/${dailyNoteWidgetData.max_home_coin}"
        )
    }

    override suspend fun onUpdateContent(intent: Intent?): RemoteViews? {
        val textIds = intArrayOf(
            R.id.resin_text,
            R.id.daily_task_text,
            R.id.home_coin_text,
        )

        setCommonStyle(
            appWidgetBinding.configuration,
            textIds
        )

        return super.onUpdateContent(intent)
    }

}