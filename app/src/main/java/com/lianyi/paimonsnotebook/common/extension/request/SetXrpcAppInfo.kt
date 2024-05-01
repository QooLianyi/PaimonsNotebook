package com.lianyi.paimonsnotebook.common.extension.request

import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import okhttp3.Request


/*
*
* 设置请求头app信息
* */
fun Request.Builder.setXRPCAppInfo(
    clientType: String = CoreEnvironment.ClientType,
    appId: String = "bll8iq97cem8"
) {
    addHeader("x-rpc-app_version", CoreEnvironment.XrpcVersion)
    addHeader("x-rpc-client_type", clientType)
    addHeader("x-rpc-app_id", appId)
}