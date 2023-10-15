package com.lianyi.paimonsnotebook.ui.screen.home.service

import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.web.hoyolab.api_sdk.combo_panda.QRCodeClient
import com.lianyi.paimonsnotebook.common.web.hoyolab.api_sdk.combo_panda.QRCodeParamData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.auth.AuthClient

/*
* 二维码扫描服务
*
* */
class HoyolabQRCodeService {

    private val qrCodeClient by lazy {
        QRCodeClient()
    }
    private val authClient by lazy {
        AuthClient()
    }

    private val map = mutableMapOf<String, String>()

    private lateinit var param: QRCodeParamData

    //扫描是否成功
    var scanSuccess: Boolean = false
        private set

    suspend fun scan(
        qrCodeContent: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        map.clear()
        map += try {
            qrCodeContent.split("?").last().split("&").map {
                val split = it.split("=")
                split.first().trim() to split.last().trim()
            }.toMap()
        } catch (_: Exception) {
            mapOf()
        }

        param = QRCodeParamData(
            appId = (map["app_id"] ?: "").toIntOrNull() ?: -1,
            gameBiz = map["biz_key"] ?: "",
            ticket = map["ticket"] ?: ""
        )

        if (!param.isAvailable()) {
            onError.invoke("扫描失败:缺少参数")
            map.clear()
            return
        }

        val result = qrCodeClient.scan(param)

        scanSuccess = result.success

        if (result.success) {
            onSuccess.invoke()
        } else {
            onError.invoke("扫描失败:${result.message}")
        }

        map.clear()
    }

    suspend fun confirm(
        user: User,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (!this::param.isInitialized) {
            onError.invoke("参数未初始化")
            return
        }

        val result = authClient.getGameToken(user.userEntity)

        if (!result.success) {
            onError.invoke("获取GameToken失败:${result.message}")
            return
        }

        val confirm = qrCodeClient.confirm(user.userEntity.aid, result.data, param)
        if (confirm.success) {
            onSuccess.invoke()
        } else {
            onError.invoke("确认登录失败:${confirm.message}")
        }
    }
}