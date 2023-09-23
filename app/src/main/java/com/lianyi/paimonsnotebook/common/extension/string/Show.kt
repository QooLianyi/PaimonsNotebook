package com.lianyi.paimonsnotebook.common.extension.string

import android.widget.Toast
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication

/*
* toast拓展
*
* */

fun String.show(){
    Toast.makeText(PaimonsNotebookApplication.context, this, Toast.LENGTH_SHORT).show()
}