package com.lianyi.paimonsnotebook.common.util.system_service

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Service
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import java.io.File

@SuppressLint("InternalInsetResource", "DiscouragedApi")
object SystemService {

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    private val PERMISSION_REQUEST_CODE by lazy {
        1200
    }

    val statusBarHeight by lazy {
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            context.resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    val screenWidthPx by lazy {
        context.resources.displayMetrics.widthPixels
    }
    val screenHeightPx by lazy {
        context.resources.displayMetrics.heightPixels
    }

    private val clipboardManager by lazy {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    private val windowManager by lazy {
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    //设置剪切板内容
    fun setClipBoardText(text: String, label: String? = null) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    //安装程序
    fun installAndroidApplication(file: File) {
        context.startActivity(
            Intent(Intent.ACTION_VIEW).apply {
                val uri =
                    FileProvider.getUriForFile(context, FileHelper.provider, file)
                setDataAndType(uri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            }
        )
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

    fun requestPermission(activity: Activity, vararg list: String) {
        ActivityCompat.requestPermissions(activity, list, PERMISSION_REQUEST_CODE)
    }

}