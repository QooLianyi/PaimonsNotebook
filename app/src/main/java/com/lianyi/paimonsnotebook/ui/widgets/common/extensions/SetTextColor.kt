package com.lianyi.paimonsnotebook.ui.widgets.common.extensions

import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import com.lianyi.paimonsnotebook.ui.theme.Black

fun RemoteViews.setTextColor(viewId: Int, color: Int?) =
    this.setTextColor(viewId,color?: Black.toArgb())