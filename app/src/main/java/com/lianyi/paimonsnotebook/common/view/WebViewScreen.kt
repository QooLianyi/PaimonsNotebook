package com.lianyi.paimonsnotebook.common.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.WindowManager
import android.webkit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WebViewScreen : ComponentActivity() {

    private val webUrl:String by lazy {
        intent.getStringExtra("url")?:"https://user.mihoyo.com"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WebView.setWebContentsDebuggingEnabled(true)

        setContent {
            PaimonsNotebookTheme {
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