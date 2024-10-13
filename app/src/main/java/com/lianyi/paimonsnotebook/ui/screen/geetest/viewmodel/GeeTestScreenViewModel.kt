package com.lianyi.paimonsnotebook.ui.screen.geetest.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel

class GeeTestScreenViewModel : ViewModel() {
    fun init(intent: Intent?, onFail: () -> Unit) {

        if (intent == null) {
            onFail.invoke()
            return
        }

    }


}