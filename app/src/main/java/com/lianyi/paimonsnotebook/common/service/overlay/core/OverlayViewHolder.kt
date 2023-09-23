package com.lianyi.paimonsnotebook.common.service.overlay.core

import android.view.Gravity
import android.view.WindowManager

class OverlayViewHolder(val params: WindowManager.LayoutParams, service: OverlayService) {
    val view = getOverlayView(service)

    init {
        params.gravity = Gravity.TOP or Gravity.START
    }
}