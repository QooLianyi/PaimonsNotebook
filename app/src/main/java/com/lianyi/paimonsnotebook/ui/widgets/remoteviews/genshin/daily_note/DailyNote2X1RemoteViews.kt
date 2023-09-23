package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note

import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setBackgroundResource
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setTextColor
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon2X1

class DailyNote2X1RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon2X1::class.java,
    R.layout.widget_layout_daily_note_2_1
) {

    init {
        setOnClickPendingIntent(R.id.container, updatePendingIntent)
    }

    override suspend fun setDailyNote(dailyNoteData: DailyNoteData) {
        setTextViewText(
            R.id.resin_text,
            "${dailyNoteData.current_resin}/${dailyNoteData.max_resin}"
        )
        setTextViewText(
            R.id.daily_task_text,
            "${dailyNoteData.finished_task_num}/${dailyNoteData.total_task_num}"
        )
        setTextViewText(
            R.id.home_coin_text,
            "${dailyNoteData.current_home_coin}/${dailyNoteData.max_home_coin}"
        )
        setTextViewText(
            R.id.secret_tower_text,
            "${dailyNoteData.remain_resin_discount_num}/${dailyNoteData.resin_discount_num_limit}"
        )
    }

    override suspend fun onUpdateContent(intent: Intent?): RemoteViews? {
        setBackgroundResource(
            R.id.container,
            AppWidgetHelper.getAppWidgetBackgroundResource(appWidgetBinding.configuration.backgroundPattern)
        )

        val textColor = appWidgetBinding.configuration.textColor

        setTextColor(R.id.resin_text, textColor)
        setTextColor(R.id.daily_task_text, textColor)
        setTextColor(R.id.home_coin_text, textColor)
        setTextColor(R.id.secret_tower_text, textColor)

        return super.onUpdateContent(intent)
    }

}