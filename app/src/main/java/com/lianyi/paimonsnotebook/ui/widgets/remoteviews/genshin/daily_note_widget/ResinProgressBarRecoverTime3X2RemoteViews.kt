package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget

import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setBackgroundResource
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon3X1

/*
* 小组件树脂进度条3*2远端视图
* */
internal class ResinProgressBarRecoverTime3X2RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon3X1::class.java,
    R.layout.widget_layout_resin_3_2
) {
    init {
        setOnClickPendingIntent(R.id.container, updatePendingIntent)
    }

    override suspend fun setDailyNoteWidget(dailyNoteWidgetData: DailyNoteWidgetData) {
        val configuration = appWidgetBinding.configuration

        setProgressBar(
            R.id.progress_bar,
            dailyNoteWidgetData.max_resin,
            dailyNoteWidgetData.current_resin,
            false
        )
        setTextViewText(
            R.id.resin,
            "${dailyNoteWidgetData.current_resin}/${dailyNoteWidgetData.max_resin}"
        )

        setTextViewText(
            R.id.recover_time,
            TimeHelper.getRecoverTime(dailyNoteWidgetData.resin_recovery_time.toLongOrNull() ?: 0L)
        )

        setBackgroundResource(
            R.id.container,
            AppWidgetHelper.getAppWidgetBackgroundResource(configuration.backgroundPattern)
        )
    }
}