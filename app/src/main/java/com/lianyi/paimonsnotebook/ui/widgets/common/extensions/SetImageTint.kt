package com.lianyi.paimonsnotebook.ui.widgets.common.extensions

import android.widget.RemoteViews

fun RemoteViews.setImageTint(viewId: Int, color: Int) =
    this.setInt(viewId, "setColorFilter", color)