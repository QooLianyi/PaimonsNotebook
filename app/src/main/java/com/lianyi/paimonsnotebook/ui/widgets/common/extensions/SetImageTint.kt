package com.lianyi.paimonsnotebook.ui.widgets.common.extensions

import android.widget.RemoteViews
import androidx.compose.ui.graphics.toArgb
import com.lianyi.paimonsnotebook.ui.theme.Black

fun RemoteViews.setImageTint(viewId: Int, color: Int?) =
    this.setInt(viewId, "setColorFilter", color ?: Black.toArgb())