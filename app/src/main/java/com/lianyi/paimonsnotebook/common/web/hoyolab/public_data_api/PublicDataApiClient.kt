package com.lianyi.paimonsnotebook.common.web.hoyolab.public_data_api

import android.os.Build
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import java.util.UUID

class PublicDataApiClient {

    //获取fp
    suspend fun getFp(fp: String = "${(1000000000..9999999999).random()}") = buildRequest {
        url(ApiEndpoints.getFp)

        buildMap {
            put("device_id", CoreEnvironment.DeviceId)
            put("bbs_device_id", CoreEnvironment.BBSDeviceId)
            put("seed_id", UUID.randomUUID().toString())
            put("seed_time", "${System.currentTimeMillis()}")
            put("platform", CoreEnvironment.ClientType)
            put("device_fp", fp)
            put("app_name", "bbs_cn")
            put(
                "ext_fields",
                "{\"proxyStatus\":0,\"isRoot\":0,\"romCapacity\":\"${
                    Runtime.getRuntime().freeMemory() / (1024 * 1024)
                }\",\"deviceName\":\"${Build.DEVICE}\",\"productName\":\"${Build.PRODUCT}\",\"romRemain\":\"${
                    Runtime.getRuntime().maxMemory() / (1024 * 1024)
                }\",\"hostname\":\"${Build.HOST}\",\"screenSize\":\"${
                    with(
                        PaimonsNotebookApplication.context.resources.displayMetrics
                    ) {
                        "${widthPixels}x${heightPixels}"
                    }
                }\",\"isTablet\":0,\"aaid\":\"error_1008004\",\"model\":\"${Build.MODEL}\",\"brand\":\"${Build.BRAND}\",\"hardware\":\"${Build.HARDWARE}\",\"deviceType\":\"${Build.TYPE}\",\"devId\":\"REL\",\"serialNumber\":\"unknown\",\"sdCapacity\":${
                    Runtime.getRuntime().maxMemory() / (1024 * 1024)
                },\"buildTime\":\"${Build.TIME}\",\"buildUser\":\"${Build.USER}\",\"simState\":0,\"ramRemain\":\"${
                    Runtime.getRuntime().freeMemory()
                }\",\"deviceInfo\":\"${Build.DEVICE}\\/${Build.PRODUCT}/${Build.PRODUCT}:${Build.VERSION.RELEASE}\",\"vaid\":\"error_1008004\",\"buildType\":\"user\",\"sdkVersion\":\"${Build.VERSION.SDK_INT}\",\"ui_mode\":\"UI_MODE_TYPE_NORMAL\",\"isMockLocation\":0,\"cpuType\":\"arm64-v8a\",\"isAirMode\":0,\"ringMode\":2,\"chargeStatus\":3,\"manufacturer\":\"${Build.BRAND}\",\"emulatorStatus\":0,\"appMemory\":\"512\",\"osVersion\":\"${Build.VERSION.RELEASE}\",\"vendor\":\"unknown\",\"sdRemain\":${
                    (102400..256000).random()
                },\"buildTags\":\"release-keys\",\"packageName\":\"com.mihoyo.hyperion\",\"networkType\":\"WiFi\",\"oaid\":\"error_1008004\",\"debugStatus\":0,\"ramCapacity\":\"${
                    Runtime.getRuntime().freeMemory() / (1024 * 1024)
                }\",\"display\":\"${Build.DISPLAY}\",\"packageVersion\":\"2.20.1\",\"gyroscope\":\"0.0x0.0x0.0\",\"batteryStatus\":${(20..100).random()},\"hasKeyboard\":1,\"board\":\"${Build.BOARD}\"}"
            )
        }.post(this)
    }.getAsJson<GetFpData>()

    //获取参数列表
    suspend fun getExtList(platform: Int = 2) = buildRequest {
        url(ApiEndpoints.getExtList(platform))
    }.getAsJson<GetExtListData>()
}