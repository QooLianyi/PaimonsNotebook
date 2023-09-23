package com.lianyi.paimonsnotebook.common.util.system_service

import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

object SystemService {

    private val context by lazy {
        PaimonsNotebookApplication.context
    }
    private val PERMISSION_REQUEST_CODE by lazy {
        1200
    }

    private val clipboardManager by lazy {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    fun setClipBoardText(text: String, label: String? = null) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    fun serviceIsRunning(cls: Service) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    }

    fun checkPermission(vararg list: String): Boolean {
        var pass = true
        list.forEach {
            pass =
                pass && PaimonsNotebookApplication.context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
        return pass
    }

    fun requestPermission(activity: Activity,vararg list:String) {
        ActivityCompat.requestPermissions(activity, list, PERMISSION_REQUEST_CODE)
    }

}