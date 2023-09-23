package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import android.os.Build
import android.os.Environment
import android.os.StatFs
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

@Composable
fun DebugDeviceInfoContent() {
    Column {
        Text(text = "安卓版本:${Build.VERSION.RELEASE}", fontSize = 18.sp)
        Text(text = "手机品牌:${Build.BRAND}", fontSize = 18.sp)
        Text(text = "手机型号:${Build.MODEL}", fontSize = 18.sp)
        Text(text = "Build.PRODUCT:${Build.PRODUCT}", fontSize = 18.sp)
        Text(text = "Build.VERSION.SDK_IN:${Build.VERSION.SDK_INT}", fontSize = 18.sp)
        Text(text = "Build.DISPLAY:${Build.DISPLAY}", fontSize = 18.sp)
        Text(text = "Build.TYPE:${Build.TYPE}", fontSize = 18.sp)
        Text(text = "Build.HARDWARE:${Build.HARDWARE}", fontSize = 18.sp)
        Text(text = "Build.DEVICE:${Build.DEVICE}", fontSize = 18.sp)
        Text(text = "Build.ID:${Build.ID}", fontSize = 18.sp)
        Text(text = "Build.USER:${Build.USER}", fontSize = 18.sp)
        Text(text = "Build.TIME:${Build.TIME}", fontSize = 18.sp)
        Text(text = "Build.TAGS:${Build.TAGS}", fontSize = 18.sp)
        Text(text = "屏幕分辨率:${with(PaimonsNotebookApplication.context.resources.displayMetrics){
            "${widthPixels}x${heightPixels}"
        }}", fontSize = 18.sp)
        Text(text = "最大分配内存:${Runtime.getRuntime().maxMemory() / (1024 * 1024)}", fontSize = 18.sp)
        Text(text = "空闲内存:${Runtime.getRuntime().freeMemory() / (1024 * 1024)}", fontSize = 18.sp)
        Text(text = "总内存:${Runtime.getRuntime().totalMemory() / (1024 * 1024)}", fontSize = 18.sp)
        Text(text = StatFs(Environment.getRootDirectory().path).let {
            """
                blockSizeLong:${it.blockSizeLong},
                blockCountLong:${it.blockCountLong},
                availableBlocksLong:${it.availableBlocksLong}
            """.trimIndent()
        }, fontSize = 18.sp)
        Text(text = "DataDirectoryTotalSpace:${Environment.getDataDirectory().totalSpace / 1024 / 1024}", fontSize = 18.sp)
    }
}