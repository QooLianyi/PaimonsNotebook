package com.lianyi.paimonsnotebook.common.application

import android.content.Intent
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.lianyi.paimonsnotebook.common.service.overlay.debug.DebugOverlayService
import com.lianyi.paimonsnotebook.common.service.overlay.util.OverlayHelper
import com.lianyi.paimonsnotebook.common.service.util.ServiceHelper
import com.lianyi.paimonsnotebook.common.util.file.FileHelper

/*
* 程序生命周期观察者
* */
open class ApplicationLifecycleObserver : DefaultLifecycleObserver {

    override fun onPause(owner: LifecycleOwner) {
        sendCommandForDebugPanel(ServiceHelper.Command_Hide)
        super.onPause(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        sendCommandForDebugPanel(ServiceHelper.Command_Show)
        super.onResume(owner)
    }

    private fun sendCommandForDebugPanel(command: String) {
        val permission = OverlayHelper.checkPermission()
        if (FileHelper.debug && permission) {
            PaimonsNotebookApplication.context.startService(
                Intent(
                    PaimonsNotebookApplication.context,
                    DebugOverlayService::class.java
                ).putExtra(
                    ServiceHelper.Command, command
                )
            )
        }
    }
}