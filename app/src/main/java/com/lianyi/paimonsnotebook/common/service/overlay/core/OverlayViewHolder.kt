package com.lianyi.paimonsnotebook.common.service.overlay.core

import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import kotlinx.coroutines.Job

class OverlayViewHolder(val params: WindowManager.LayoutParams, service: OverlayService) {
    private val pair = getOverlayView(service)

    val view: ComposeView = pair.first
    val job: Job = pair.second

    init {
        params.gravity = Gravity.TOP or Gravity.START
    }
}