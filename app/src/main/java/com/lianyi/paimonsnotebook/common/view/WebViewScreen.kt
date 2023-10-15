package com.lianyi.paimonsnotebook.common.view

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class WebViewScreen : BaseActivity() {

    private val webUrl:String by lazy {
        intent.getStringExtra("url")?:"https://user.mihoyo.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)

        setContent {
            PaimonsNotebookTheme(hideNavigationBar = true, hideStatusBar = true) {
                AndroidView(factory = {
                    WebView(it).apply{
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        loadUrl(webUrl)
                    }
                }, modifier = Modifier.fillMaxSize())
            }
        }
    }
}