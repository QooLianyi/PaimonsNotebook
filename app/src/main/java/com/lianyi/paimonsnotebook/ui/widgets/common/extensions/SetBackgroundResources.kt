package com.lianyi.paimonsnotebook.ui.widgets.common.extensions

import android.widget.RemoteViews

fun RemoteViews.setBackgroundResource(viewId: Int, resId: Int) =
    this.setInt(viewId, "setBackgroundResource", resId)