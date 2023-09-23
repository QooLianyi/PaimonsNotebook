package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state

import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseAppWidget
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews

/*
* 小组件错误状态远端视图
* */
internal class ErrorRemoteViews(
    appWidgetId: Int,
    cls: Class<out BaseAppWidget>,
    content:String = "",
    iconResId:Int = R.drawable.emotion_icon_paimon_error
) : BaseRemoteViews(appWidgetId, cls, R.layout.appwidget_error_placeholder) {
    init {
        setImageViewResource(R.id.image,iconResId)
        if(content.isNotBlank()){
            setTextViewText(R.id.text,content)
        }
        setOnClickPendingIntent(R.id.container, updatePendingIntent)
    }
}