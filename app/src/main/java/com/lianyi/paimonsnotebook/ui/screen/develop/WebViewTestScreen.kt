package com.lianyi.paimonsnotebook.ui.screen.develop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.White

class WebViewTestScreen : ComponentActivity() {

    //没有webview 等控件时需要使用AndroidView控件实现
    private lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaimonsNotebookTheme {
                val progression = remember {
                    mutableStateOf(0f)
                }
                Surface(color = White) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        LinearProgressIndicator(//显示加载进度条,取值范围0-1
                            progress = progression.value / 100,
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = Color.Green
                        )


                        //Android View充当Compos的桥梁，且factory只在主线程里面调用一次
                        AndroidView(
                            factory = { context ->
                                webView = WebView(context)
                                webView.apply {
                                    settings.javaScriptEnabled = true//支持javaScript脚本
                                    webViewClient = object : WebViewClient() {//创建webViewClient实例，作用是当网页内部跳转时在当前webView打开，而不是打开系统浏览器
                                    }
                                    loadUrl("https://user.mihoyo.com")
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                            /*
                            * 由于重新编译该应用程序，也可能在UI线程上多次运行update,它是设置依赖于状态的视图属性的正确位置，
                            * 当状态更改时，块将重新执行以设置新特性
                            * 在update()方法中，可以对当前webview的事件进行监听
                            * */
                            update = { webView ->
                                webView.webChromeClient = object : WebChromeClient() {
                                    //监听网页加载进度
                                    override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                        super.onProgressChanged(view, newProgress)
                                        progression.value = newProgress.toFloat()
                                    }
                                }
                            }
                        )
                    }

                }
            }
        }
    }

    //处理返回键事件
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //让webview展示过的页面能够返回
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
