package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.test

import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon2X1

class Test2X1RemoteViews(
    private val appWidgetBinding: AppWidgetBinding
) : BaseRemoteViews(
    appWidgetBinding.appWidgetId,
    AppWidgetCommon2X1::class.java,
    R.layout.widget_layout_test
) {
    override suspend fun onUpdateContent(intent: Intent?): RemoteViews? {
        setCommonStyle(appWidgetBinding.configuration)

        return super.onUpdateContent(intent)
    }
}