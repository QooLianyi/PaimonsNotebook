package com.lianyi.paimonsnotebook.common.util.system_service

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

inline fun <reified T : Activity> gotoScreen(bundle: Bundle? = null) {
    PaimonsNotebookApplication.context.apply {
        startActivity(Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("bundle",bundle)
        })
    }
}

fun gotoScreenCls(cls: Class<*>) {
    PaimonsNotebookApplication.context.apply {
        startActivity(Intent(this, cls).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
    }
}