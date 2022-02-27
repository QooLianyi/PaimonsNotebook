package com.lianyi.paimonsnotebook.lib.information

class ResponseCode {
    companion object{
        const val OK = "0" //OK
        const val LOGON_FAILURE = "-100" //登录失效
        const val PARAMETER_ERROR = "1000" //参数错误
        const val TOO_OFTEN = "-1004" //请求过于频繁
        const val LOW_LEVEL = "-120" //等级不足
        const val DAILY_NOTE_NOT_OPEN = "10103" //便笺未开启
        const val DAILY_SIGN_IS_SIGNED = "-5003" //每日签到已完成
        const val CAN_NOT_CONVERT_TO_JSON = "-501000"//不能转换为JSON
        const val TIME_OUT = "-501100"
    }
}