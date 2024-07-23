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

        const val RET_AIGIS_FAILED = -3102
        const val RET_BAN = -3201
        const val RET_CHECK_BAN = -3260
        const val RET_NEED_AIGIS = -3101
        const val RET_NEED_RISK_VERIFY = -3235
        const val RET_PROTECT_BAN = -3254
        const val RET_QR_URL_EXPIRED = -3501
        const val RET_SUCCESS = 0
        const val RET_TOKEN_INVALID = -100

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

    val validate: Boolean
        get() = retcode == NEED_VALIDATE

    val needAigis: Boolean
        get() = retcode == RET_NEED_AIGIS

    var responseHeaders: List<Pair<String, String>>? = null
        private set

    fun setResponseHeaders(list: List<Pair<String, String>>) {
        responseHeaders = list
    }
}