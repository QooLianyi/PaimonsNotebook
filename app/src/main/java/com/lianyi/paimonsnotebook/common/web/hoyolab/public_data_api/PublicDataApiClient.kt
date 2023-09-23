package com.lianyi.paimonsnotebook.common.web.hoyolab.public_data_api

import android.os.Build
import android.os.Environment
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints

class PublicDataApiClient {

    //获取fp
    suspend fun getFp(fp: String = "${(1000000000..9999999999).random()}") = buildRequest {
        url(ApiEndpoints.getFp)

        buildMap {
            put("device_id", CoreEnvironment.DeviceId)
            put("seed_id", getRandomChars(16))
            put("seed_time", "${System.currentTimeMillis()}")
            put("platform", "2")
            put("device_fp", fp)
            put("app_name", "bbs_cn")
            put(
                "ext_fields",
                "{\"hostname\":\"ubuntu\",\"buildTime\":\"1682413330000\",\"romCapacity\":\"${
                    Runtime.getRuntime().maxMemory() / (1024 * 1024)
                }\",\"ramCapacity\":\"${Environment.getDataDirectory().totalSpace / 1024 / 1024}\",\"board\":\"${Build.BOARD}\",\"romRemain\":\"${
                    Runtime.getRuntime().freeMemory() / (1024 * 1024)
                }\",\"appMemory\":\"${
                    Runtime.getRuntime().freeMemory() / (1024 * 1024)
                }\",\"display\":\"${Build.DISPLAY}\",\"brand\":\"${Build.BRAND}\",\"productName\":\"${Build.PRODUCT}\",\"deviceInfo\":\"\",\"hardware\":\"${Build.HARDWARE}\",\"model\":\"${Build.MODEL}\"}\n"
            )
        }.post(this)
    }.getAsJson<GetFpData>()

    //获取参数列表
    suspend fun getExtList(platform:Int = 2) = buildRequest {
        url(ApiEndpoints.getExtList(platform))
    }.getAsJson<GetExtListData>()

    private fun getRandomChars(times: Int) = with(StringBuilder()) {
        val range = "abcdefghijklmnopqrstuvwxyz1234567890"
        repeat(times) {
            this.append(range.random())
        }
        this.toString()
    }

}