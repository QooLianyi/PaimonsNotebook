package com.lianyi.paimonsnotebook.ui.screen.account.util

import com.geetest.sdk.GT3ErrorBean
import com.geetest.sdk.GT3Listener

class GT3ListenerImpl(
    private val onButtonClick: () -> Unit,
    private val onDialogResult: (String) -> Unit
) : GT3Listener() {
    override fun onReceiveCaptchaCode(p0: Int) {
    }

    override fun onStatistics(p0: String?) {
    }

    override fun onClosed(p0: Int) {
    }

    override fun onSuccess(p0: String?) {
    }

    override fun onFailed(p0: GT3ErrorBean?) {
    }

    override fun onButtonClick() {
        onButtonClick.invoke()
    }

    override fun onDialogResult(result: String?) {
        onDialogResult.invoke(result ?: "")
    }
}