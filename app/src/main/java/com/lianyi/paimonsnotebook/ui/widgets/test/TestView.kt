package com.lianyi.paimonsnotebook.ui.widgets.test

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.ui.widgets.widget.AppWidgetCommon1X1

class TestView {
    val context by lazy {
        PaimonsNotebookApplication.context
    }
    fun test(){
        View(context).apply {
            AppWidgetCommon1X1().apply {

            }
        }
    }
}