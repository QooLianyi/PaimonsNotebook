package com.lianyi.core.common.notify

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class NotifyData(
    val content: String,
    val notificationId: String,
    val type: NotifyType = NotifyType.Normal,
    val closeable: Boolean = false,
    val autoDismissTime: Long = 3000,
    val keepShow: Boolean = false,
    val isShowing: MutableState<Boolean> = mutableStateOf(true)
)
