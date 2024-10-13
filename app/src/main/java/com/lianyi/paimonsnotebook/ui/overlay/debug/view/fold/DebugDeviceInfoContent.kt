package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import android.annotation.SuppressLint
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirst

@SuppressLint("HardwareIds")
@Composable
fun DebugDeviceInfoContent() {

    var DeviceId by remember {
        mutableStateOf("")
    }
    var BBSDeviceId by remember {
        mutableStateOf("")
    }
    var DeviceId40 by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        dataStoreValuesFirst {
            DeviceId = it[PreferenceKeys.DeviceId] ?: ""
            BBSDeviceId = it[PreferenceKeys.BBSDeviceId] ?: ""
            DeviceId40 = it[PreferenceKeys.DeviceId40] ?: ""
        }
    }

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
        Text(
            text = "屏幕分辨率:${
                with(PaimonsNotebookApplication.context.resources.displayMetrics) {
                    "${widthPixels}x${heightPixels}"
                }
            }", fontSize = 18.sp
        )
        Text(
            text = "最大分配内存:${Runtime.getRuntime().maxMemory() / (1024 * 1024)}",
            fontSize = 18.sp
        )
        Text(
            text = "空闲内存:${Runtime.getRuntime().freeMemory() / (1024 * 1024)}",
            fontSize = 18.sp
        )
        Text(
            text = "总内存:${Runtime.getRuntime().totalMemory() / (1024 * 1024)}",
            fontSize = 18.sp
        )
        Text(text = StatFs(Environment.getRootDirectory().path).let {
            """
                blockSizeLong:${it.blockSizeLong},
                blockCountLong:${it.blockCountLong},
                availableBlocksLong:${it.availableBlocksLong}
            """.trimIndent()
        }, fontSize = 18.sp)
        Text(
            text = "DataDirectoryTotalSpace:${Environment.getDataDirectory().totalSpace / 1024 / 1024}",
            fontSize = 18.sp
        )

        Text(
            text = "安卓ID:${
                Settings.Secure.getString(
                    PaimonsNotebookApplication.context.contentResolver,
                    "android_id"
                )
            }", fontSize = 18.sp
        )

        Text(text = "BBS INFO", fontSize = 18.sp)
        Text(text = "DeviceId:${DeviceId}", fontSize = 18.sp)
        Text(text = "DeviceId40:${DeviceId40}", fontSize = 18.sp)
        Text(text = "BBSDeviceId:${BBSDeviceId}", fontSize = 18.sp)
    }
}