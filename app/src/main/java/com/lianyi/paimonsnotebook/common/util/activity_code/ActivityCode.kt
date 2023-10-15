package com.lianyi.paimonsnotebook.common.util.activity_code

/*
* 各个activity的代号
* 用于在跳转,返回结果时识别来源的activity
* */
object ActivityCode {
    const val EXTRA_REQUEST_CODE = "activity_request_code"
    const val EXTRA_RESULT_CODE = "activity_result_code"

    //首页
    const val HomeScreen = 100
    //二维码扫描
    const val QRCodeScanActivity = 105
}