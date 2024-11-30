package com.lianyi.paimonsnotebook.common.util.system_service

import android.os.Build

/*
* 判断sdk版本是否低于29(android 9)
* */
fun sdkVersionLessThanOrEqualTo29(
    inConform: () -> Unit = {},
    finally: () -> Unit = {},
    conform: () -> Unit
) = sdkVersionLessThan(
    inConform = inConform,
    finally = finally,
    conform = conform,
    target = Build.VERSION_CODES.Q
)

fun sdkVersionLessThanOrEqualTo34(
    inConform: () -> Unit = {},
    finally: () -> Unit = {},
    conform: () -> Unit
) = sdkVersionLessThan(
    inConform = inConform,
    finally = finally,
    conform = conform,
    target = Build.VERSION_CODES.UPSIDE_DOWN_CAKE
)

fun sdkVersionLessThan(
    target: Int,
    inConform: () -> Unit = {},
    finally: () -> Unit = {},
    conform: () -> Unit
) {
    if (Build.VERSION.SDK_INT <= target) {
        conform.invoke()
    } else {
        inConform.invoke()
    }

    finally.invoke()
}