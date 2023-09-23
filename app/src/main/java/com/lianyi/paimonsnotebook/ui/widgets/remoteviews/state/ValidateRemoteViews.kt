package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state

import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseAppWidget
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews

/*
* 小组件需要验证状态远端视图
* */
internal class ValidateRemoteViews(
    appWidgetId:Int,
    cls: Class<out BaseAppWidget>
) : BaseRemoteViews(appWidgetId, cls, R.layout.appwidget_error_placeholder) {
    init {
        setTextViewText(R.id.text,"需要验证")
        setImageViewResource(R.id.image,R.drawable.emotion_icon_ambor_failure)
        setOnClickPendingIntent(R.id.container, goValidatePendingIntent)
    }
}