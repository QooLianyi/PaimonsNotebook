package com.lianyi.paimonsnotebook.common.service.overlay.debug

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.service.overlay.core.OverlayService
import com.lianyi.paimonsnotebook.common.service.util.ServiceHelper
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.DebugOverlayContent
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.DebugOverlayControllerContent
import com.lianyi.paimonsnotebook.ui.screen.home.view.HomeScreen

class DebugOverlayService : OverlayService(
    serviceId = ServiceHelper.overlayServiceId,
    channelId = ServiceHelper.overlayChannelId,
    channelName = ServiceHelper.overlayChannelName,
    notification = with(
        NotificationCompat.Builder(
            context,
            ServiceHelper.overlayChannelId
        )
    ) {
        val pendingIntent = Intent(context, HomeScreen::class.java).let {
            PendingIntent.getActivity(context, 0, it, PendingIntent.FLAG_IMMUTABLE)
        }
        val exitIntent = Intent(context, DebugOverlayService::class.java).apply {
            putExtra(ServiceHelper.Command, ServiceHelper.Command_Exit)
        }
        val exitPendingIntent =
            PendingIntent.getService(context, 0, exitIntent, PendingIntent.FLAG_IMMUTABLE)
        this.setContentTitle("调试面板已启用")
            .setSmallIcon(R.drawable.icon_klee_square)
            .setContentIntent(pendingIntent)
            .addAction(0, "关闭调试面板", exitPendingIntent)
            .build()
    },
    controllerContent = {
        DebugOverlayControllerContent()
    },
    content = {
        DebugOverlayContent(it)
    },
    onStartCommandBlock = { overlayState, command ->
        println("command = ${command}")
        when(command){
            ServiceHelper.Command_Show->{
                overlayState.showController = true
            }
            ServiceHelper.Command_Hide->{
                overlayState.showController = false
                overlayState.showContent = false
            }
        }
    }
) {
    companion object {
        private val context by lazy {
            PaimonsNotebookApplication.context
        }
    }
}