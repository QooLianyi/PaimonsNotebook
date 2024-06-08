package com.lianyi.paimonsnotebook.common.util.system_service

import android.os.Build

/*
* 判断sdk版本是否低于29(android 9)
* */
fun sdkVersionLessThanOrEqualTo29(
    inConform: () -> Unit = {},
    finally: () -> Unit = {},
    conform: () -> Unit
) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        conform.invoke()
    } else {
        inConform.invoke()
    }

    finally.invoke()
}