package com.lianyi.paimonsnotebook.common.util.notification

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PaimonsNotebookNotificationData(
    val content:String,
    val notificationId:String,
    val type:PaimonsNotebookNotificationType = PaimonsNotebookNotificationType.Normal,
    val closeable:Boolean = false,
    val autoDismissTime:Long = 3000,
    val keepShow:Boolean = false,
    val isShowing:MutableState<Boolean> = mutableStateOf(true)
)
