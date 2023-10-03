package com.lianyi.paimonsnotebook.common.data

data class ResultData<T>(
    val message: String,
    val retcode: Int,
    val data: T,
) {
    companion object {
        //成功
        const val SUCCESS_CODE = 0

        //需要验证
        const val NEED_VALIDATE = 1034

        //错误的cookie
        const val ERROR_COOKIE = 10104

        //ds错误
        const val INVALID_REQUEST_CODE = -10001

        //网络错误
        const val NETWORK_ERROR = -200

        //未知错误
        const val UNKNOWN_EXCEPTION = -201

        //响应体转换异常
        const val RESPONSE_CONVERT_EXCEPTION = -202

        fun <T> getSuccessResult(data: T) =
            ResultData(message = "success", retcode = SUCCESS_CODE, data = data)
    }

    val errorMsg: String
        get() = "${retcode}:${message}"

    val success: Boolean
        get() = retcode == SUCCESS_CODE

    val validate:Boolean
        get() = retcode == NEED_VALIDATE
}