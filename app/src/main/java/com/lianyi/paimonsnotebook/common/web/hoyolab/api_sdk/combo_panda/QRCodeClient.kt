package com.lianyi.paimonsnotebook.common.web.hoyolab.api_sdk.combo_panda

import com.google.gson.JsonObject
import com.lianyi.paimonsnotebook.common.core.enviroment.CoreEnvironment
import com.lianyi.paimonsnotebook.common.data.EmptyData
import com.lianyi.paimonsnotebook.common.extension.request.setHost
import com.lianyi.paimonsnotebook.common.extension.request.setReferer
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.request.buildRequest
import com.lianyi.paimonsnotebook.common.util.request.getAsJson
import com.lianyi.paimonsnotebook.common.util.request.post
import com.lianyi.paimonsnotebook.common.web.ApiEndpoints
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth.GameTokenData

class QRCodeClient {
    suspend fun scan(param: QRCodeParamData) =
        buildRequest {
            url(ApiEndpoints.getQRCodeScan(param.gameBiz).apply {
                println("url = ${this}")
            })

            buildMap {
                put("app_id", param.appId)
                put("device", CoreEnvironment.DeviceId)
                put("ticket", param.ticket)
            }.post(this)

            setHost(ApiEndpoints.ApiSdkHost)
            setReferer(ApiEndpoints.AppMihoyoReferer)

        }.getAsJson<EmptyData>()

    suspend fun confirm(
        aid: String,
        gameTokenData: GameTokenData,
        param: QRCodeParamData
    ) =
        buildRequest {
            url(ApiEndpoints.getQRCodeConfirm(param.gameBiz))

            val raw = "{\"uid\":\"${aid}\",\"token\":\"${gameTokenData.game_token}\"}"

            buildMap {
                put("app_id", param.appId)
                put("device", CoreEnvironment.DeviceId)
                put(
                    "payload",
                    GamePayloadData(proto = "Account", raw = raw)
                )
                put("ticket", param.ticket)
            }.post(this)

            setHost(ApiEndpoints.ApiSdkHost)
            setReferer(ApiEndpoints.AppMihoyoReferer)

        }.getAsJson<EmptyData>()

}