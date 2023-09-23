package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.genshin.daily_note_widget

import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteWidgetData
import com.lianyi.paimonsnotebook.ui.widgets.common.extensions.setBackgroundResource
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon1X1

/*
* 小组件环形洞天宝钱进度条1*1远端视图
* */
internal class HomeCoinRingProgressBar1X1RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon1X1::class.java,
    R.layout.widget_layout_1_1_progress_bar
) {
    init {
        setOnClickPendingIntent(R.id.container, updatePendingIntent)
    }

    override suspend fun setDailyNoteWidget(dailyNoteWidgetData: DailyNoteWidgetData) {
        setProgressBar(
            R.id.progress_bar,
            dailyNoteWidgetData.max_home_coin,
            dailyNoteWidgetData.current_home_coin,
            false
        )
        setImageViewResource(R.id.icon,R.drawable.icon_home_coin)
    }

    override suspend fun onUpdateContent(intent: Intent?): RemoteViews? {
        setBackgroundResource(
            R.id.container,
            AppWidgetHelper.getAppWidgetBackgroundResource(appWidgetBinding.configuration.backgroundPattern)
        )

        return super.onUpdateContent(intent)
    }
}