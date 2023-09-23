package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state

import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseAppWidget
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews

/*
* 小组件未绑定状态远端视图
* */
internal class NoBindingRemoteViews(
    appWidgetId: Int,
    cls: Class<out BaseAppWidget>
) : BaseRemoteViews(appWidgetId, cls, R.layout.appwidget_no_binding_placeholder) {
    init {
        setOnClickPendingIntent(R.id.container, goBindPendingIntent)
    }
}