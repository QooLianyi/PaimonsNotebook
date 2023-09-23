package com.lianyi.paimonsnotebook.common.extension.string

import com.lianyi.paimonsnotebook.common.util.notification.PaimonsNotebookNotification
import com.lianyi.paimonsnotebook.common.util.notification.PaimonsNotebookNotificationType

fun String.notify(
    type: PaimonsNotebookNotificationType = PaimonsNotebookNotificationType.Normal,
    closeable: Boolean = false,
    autoDismissTime: Long = 3000,
    keepShow: Boolean = false,
) = PaimonsNotebookNotification.add(
    content = this,
    type = type,
    closeable = closeable,
    autoDismissTime = autoDismissTime,
    keepShow = keepShow
)

fun String.errorNotify(closeable: Boolean = true) = this.notify(
    type = PaimonsNotebookNotificationType.Error,
    closeable = closeable
)

fun String.warnNotify(closeable: Boolean = true) = this.notify(
    type = PaimonsNotebookNotificationType.Warning,
    closeable = closeable
)