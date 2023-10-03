package com.lianyi.paimonsnotebook.common.extension.intent

import android.content.ComponentName
import android.content.Intent
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

/*
* 为intent快速设置component
* */
fun Intent.setComponentName(targets: Class<out Any>) {
    this.component = ComponentName(PaimonsNotebookApplication.context, targets)
}