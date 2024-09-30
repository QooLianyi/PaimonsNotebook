package com.lianyi.paimonsnotebook.common.util.notification

import androidx.compose.runtime.mutableStateListOf
import com.lianyi.paimonsnotebook.common.extension.scope.withContextMain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

object PaimonsNotebookNotification {

    val notifications = mutableStateListOf<PaimonsNotebookNotificationData>()

    private const val randRange = "abcdefghijklmnopqrstuvwxyz1234567890"

    private val mutex = Mutex()

    //显示一个通知,返回通知的ID
    fun add(
        content: String,
        type: PaimonsNotebookNotificationType = PaimonsNotebookNotificationType.Normal,
        closeable: Boolean = false,
        autoDismissTime: Long = 3000,
        keepShow: Boolean = false,
    ): String {
        val data = PaimonsNotebookNotificationData(
            notificationId = generateNotificationId(),
            content = content,
            type = type,
            closeable = closeable,
            autoDismissTime = autoDismissTime,
            keepShow = keepShow
        )

        CoroutineScope(Dispatchers.Unconfined).launch {
            mutex.withLock {  }

            withContextMain {
                notifications.add(data)
            }

            //设置自动消失
            if (!(data.keepShow || data.closeable)) {
                delay(data.autoDismissTime)
                delayRemove(data)
            }
        }

        return data.notificationId
    }

    private suspend fun delayRemove(data: PaimonsNotebookNotificationData, delayTime: Long = 500L) {
        data.isShowing.value = false

        remove(data.notificationId)
    }

    private suspend fun remove(id: String) {
        mutex.withLock {
            notifications.removeIf { it.notificationId == id }
        }
    }

    fun removeNotifyById(id: String) {
        CoroutineScope(Dispatchers.Unconfined).launch {
            remove(id)
        }
    }

    //用于生成通知ID
    private fun generateNotificationId(): String {
        val timestamp = System.currentTimeMillis()
        val sb = StringBuilder()
        repeat(6) {
            sb.append(randRange.random())
        }
        val p1 = sb.toString()
        sb.clear()
        repeat(6) {
            sb.append(randRange.random())
        }
        val p2 = sb.toString()
        sb.clear()
        return "${timestamp}-${p1}-${p2}"
    }

}