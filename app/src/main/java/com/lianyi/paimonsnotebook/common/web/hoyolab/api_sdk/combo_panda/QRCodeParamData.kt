package com.lianyi.paimonsnotebook.common.web.hoyolab.api_sdk.combo_panda


/*
* 二维码参数类
* 非接口返回值
* */
data class QRCodeParamData(
    val appId: Int,
    val gameBiz: String,
    val ticket: String
) {
    //检查是否可用
    fun isAvailable() = appId != -1 && gameBiz.isNotEmpty() && ticket.isNotEmpty()
}