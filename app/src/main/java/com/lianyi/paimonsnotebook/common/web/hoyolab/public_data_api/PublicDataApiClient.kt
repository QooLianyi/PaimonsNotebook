package com.lianyi.paimonsnotebook.common.web.hoyolab.public_data_api

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.extension.string.toJSON
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.util.request.toRequestBody
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import org.json.JSONObject

class PublicDataApiClient {

    //获取fp
    suspend fun getFp(fp: String = "${(1000000000..9999999999).random()}") = buildRequest {
        url(ApiEndpoints.getFp)

        post(
            JSONObject().apply {
                put("device_id", CoreEnvironment.BBSDeviceId)
                put("seed_id", CoreEnvironment.DeviceIdSeed)
                put("seed_time", "${CoreEnvironment.DeviceIdSeedTime}")
                put("platform", CoreEnvironment.ClientType)
                put("device_fp", fp)
                put("app_name", "bbs_cn")
                put("ext_fields", getExtFields())
                put("bbs_device_id", CoreEnvironment.DeviceId)
            }.toString().toRequestBody()
        )

    }.getAsJson<GetFpData>()

    //获取参数列表
    suspend fun getExtList(platform: Int = 2) = buildRequest {
        url(ApiEndpoints.getExtList(platform))
    }.getAsJson<GetExtListData>()


    private fun getExtFields(): String {
        val stat = StatFs(Environment.getDataDirectory().path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availableBlocks = stat.availableBlocksLong

        val romTotal = totalBlocks * blockSize
        val romRemain = availableBlocks * blockSize

        val memoryInfo = ActivityManager.MemoryInfo()
        val activityManager =
            PaimonsNotebookApplication.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(memoryInfo)

        val ramTotal = memoryInfo.totalMem
        val ramAvail = memoryInfo.availMem

        val runtime = Runtime.getRuntime()
        val usedMem = (runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024)

        val map = buildMap {
            put("androidId", "")
            put("serialNumber", "unknown")
            put("board", Build.BOARD)
            put("brand", Build.BRAND)
            put("hardware", Build.HARDWARE)
            put("cpuType", "arm64-v8a")
            put("deviceType", Build.MODEL)
            put("display", Build.DISPLAY)
            put("hostname", Build.HOST)
            put("manufacturer", "Xiaomi")
            put("productName", Build.MODEL)
            put("model", Build.MODEL)
            put(
                "deviceInfo",
                "${Build.DEVICE}/${Build.PRODUCT}/${Build.PRODUCT}:${Build.VERSION.RELEASE}/${Build.MODEL}/${Build.TIME}:${Build.USER}/${Build.TAGS}"
            )
            put("sdkVersion", "${Build.VERSION.SDK_INT}")
            put("osVersion", Build.VERSION.RELEASE)
            put("devId", "REL")
            put("buildTags", Build.TAGS)
            put("buildType", Build.TYPE)
            put("buildUser", Build.USER)
            put("buildTime", Build.TIME)
            put("screenSize",
                with(
                    PaimonsNotebookApplication.context.resources.displayMetrics
                ) {
                    "${widthPixels}x${heightPixels}"
                }
            )
            put("networkType", "WiFi")
            put("vendor", "unknown")
            put("romCapacity", "$romTotal")
            put("romRemain", "$romRemain")
            put("ramCapacity", "$ramTotal")
            put("ramRemain", "$ramAvail")
            put("appMemory", "$usedMem")
            put("accelerometer", "0.10001241x9.800007x0.1999938")
            put("magnetometer", "15.625x-28.25x-32.625")
            put("gyroscope", "0.0x0.0x0.0")
            put("isRoot", 0)
            put("debugStatus", 0)
            put("proxyStatus", 0)
            put("emulatorStatus", 0)
            put("isTablet", 0)
            put("simState", 0)
            put("ui_mode", "UI_MODE_TYPE_NORMAL")
            put("sdCapacity", "$romTotal")
            put("sdRemain", "$romRemain")
            put("hasKeyboard", 0)
            put("isMockLocation", 0)
            put("ringMode", 2)
            put("isAirMode", 0)
            put("batteryStatus", (60..100).random())
            put("chargeStatus", (0..1).random())
            put("appInstallTimeDiff", System.currentTimeMillis() - (9999..99999).random() * 100)
            put("appUpdateTimeDiff", System.currentTimeMillis() - (1000..9999).random() * 10)
            put("deviceName", Build.MODEL)
            put("packageName", "com.mihoyo.hyperion")
            put("packageVersion", "2.29.0")
            put("aaid", "")
            put("oaid", "")
            put("vaid", "")
        }

        return JSONObject(map).toString()
    }

}