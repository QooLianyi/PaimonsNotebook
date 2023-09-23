package com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state

import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseAppWidget
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews

/*
* 小组件空状态远端视图
* */
internal class EmptyRemoteViews(
    content: String = "空类型组件",
) : BaseRemoteViews(-1, BaseAppWidget::class.java, R.layout.appwidget_error_placeholder) {
    init {
        setTextViewText(R.id.text, content)
    }
}