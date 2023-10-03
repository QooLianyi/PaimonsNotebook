package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget

import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon2X2

/*
* 小组件树脂进度条2*2远端视图
* */
internal class ResinRecoverTime2X1RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon2X2::class.java,
    R.layout.widget_layout_resin_2_1_recover_time
) {

    init {
        setOnClickPendingIntent(R.id.container, updatePendingIntent)
    }

    override suspend fun setDailyNoteWidget(dailyNoteWidgetData: DailyNoteWidgetData) {
        setTextViewText(
            R.id.resin,
            "${dailyNoteWidgetData.current_resin}/${dailyNoteWidgetData.max_resin}"
        )

        val time = TimeHelper.timeStampParseToTextDayAndHour(
            (dailyNoteWidgetData.resin_recovery_time.toLongOrNull() ?: 0L) * 1000L
        )

        setTextViewText(R.id.recover_time, "恢复时间:${time}")
    }

    override suspend fun onUpdateContent(intent: Intent?): RemoteViews? {
        setCommonStyle(
            appWidgetBinding.configuration,
            textIds = intArrayOf(R.id.recover_time, R.id.resin_subtext, R.id.resin)
        )

        return super.onUpdateContent(intent)
    }
}