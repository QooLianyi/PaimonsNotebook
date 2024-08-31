package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note

import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsContentHelper
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon2X1

class DailyNoteOverview3X2RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon2X1::class.java,
    R.layout.widget_layout_daily_note_overview_3_2,
    161f
) {
    override suspend fun setDailyNote(dailyNoteData: DailyNoteData) {
        setTextViewText(
            R.id.nick_name,
            appWidgetBinding.configuration.bindingGameRole?.nickname ?: ""
        )

        setTextViewText(
            R.id.resin_text,
            "${dailyNoteData.current_resin}"
        )

        val second = dailyNoteData.resin_recovery_time.toLongOrNull() ?: 0L

        setTextViewText(
            R.id.recover_time,
            "${TimeHelper.getRecoverTime(second)}\n${TimeHelper.getDiffDayText(second)}"
        )

        setTextViewText(
            R.id.home_coin_text,
            "${dailyNoteData.current_home_coin}/${dailyNoteData.max_home_coin}"
        )

        setTextViewText(
            R.id.daily_task_text,
            RemoteViewsContentHelper.getDailyTaskContentByState(dailyNoteData)
        )

        setTextViewText(
            R.id.quality_convert_text,
            dailyNoteData.transformer.getRecoveryTimeText()
        )
    }

    override suspend fun onUpdateContent(intent: Intent?): RemoteViews? {

        setCommonStyle(
            appWidgetBinding.configuration, textIds = intArrayOf(
                R.id.resin_text,
                R.id.recover_time,
                R.id.home_coin_text,
                R.id.daily_task_text,
                R.id.expedition_text,
                R.id.quality_convert_text,
            )
        )

        return super.onUpdateContent(intent)
    }

}