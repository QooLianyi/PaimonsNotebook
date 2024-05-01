package com.lianyi.paimonsnotebook.common.extension.request

import android.os.Build
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import okhttp3.Request

/*
* 设置设备信息请求头
* */
fun Request.Builder.setDeviceInfoHeaders(deviceId: String = CoreEnvironment.DeviceId) {
    this.apply {
        addHeader("x-rpc-device_fp", CoreEnvironment.DeviceFp)
        addHeader("x-rpc-device_name", "${Build.BRAND} ${Build.MODEL}")
        addHeader("x-rpc-device_id", deviceId)
        addHeader("x-rpc-device_model", Build.MODEL)
    }
}