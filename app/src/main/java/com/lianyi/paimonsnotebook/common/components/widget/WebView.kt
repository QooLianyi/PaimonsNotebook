package com.lianyi.paimonsnotebook.common.components.widget

import android.net.http.SslError
import android.view.View
import android.webkit.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun WebView(url: String, block: (WebView) -> Unit = {}) {
    AndroidView(factory = {
        WebView(it).apply {

//            settings.loadWithOverviewMode = true
//            settings.domStorageEnabled = true
//            settings.blockNetworkImage = false
//            settings.useWideViewPort = true


//            settings.mixedContentMode = 0
//            setLayerType(View.LAYER_TYPE_HARDWARE, null)
//
//            settings.

//            setLayerType(View.LAYER_TYPE_HARDWARE,null)
//            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            WebView.setWebContentsDebuggingEnabled(true)
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
//                databaseEnabled = true
//
//                javaScriptCanOpenWindowsAutomatically = true
//
//                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

//                loadWithOverviewMode = true
//                useWideViewPort = true

//                allowContentAccess = true
//                allowFileAccess = true

//                cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }

//            settings.
//            settings.allowFileAccess = true;
//            settings.setSupportZoom(true);
//            settings.builtInZoomControls = true;
//            settings.useWideViewPort = true;

//            webChromeClient = WebChromeClient()
//
//            webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(
//                    view: WebView?,
//                    request: WebResourceRequest?,
//                ): Boolean {
//                    view?.loadUrl(url)
//                    return true
//                }
//
//                override fun onReceivedSslError(
//                    view: WebView?,
//                    handler: SslErrorHandler?,
//                    error: SslError?,
//                ) {
//                    handler?.proceed()
//                }
//            }
            loadUrl(url)
        }
    }, modifier = Modifier.fillMaxSize()) {
        block(it)
    }
}