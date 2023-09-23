package com.lianyi.paimonsnotebook.common.service.overlay.util

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication


object OverlayHelper {
    private val context get() = PaimonsNotebookApplication.context

    fun checkPermission(): Boolean = Settings.canDrawOverlays(context)

    fun requestPermission(activity: ActivityResultLauncher<Intent>) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        activity.launch(intent)
    }
}