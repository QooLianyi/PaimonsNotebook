package com.lianyi.paimonsnotebook.ui.screen.login.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.extension.string.showLong
import com.lianyi.paimonsnotebook.ui.screen.login.viewmodel.MihoyoLoginViewModel
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White

class MihoyoLoginActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[MihoyoLoginViewModel::class.java]
    }

    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.activity = this

        "登录完成后点击[我已登录]按钮以进行下一步".showLong()

        setContentView(R.layout.mihoyo_login_acitivity_layout)

        val frameLayout = findViewById<FrameLayout>(R.id.webview_container)

        webView = WebView(PaimonsNotebookApplication.context).apply {
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
            }
            loadUrl(viewModel.loginUrl)
        }

        frameLayout.addView(webView)

        val next = findViewById<CardView>(R.id.next)

        next.setOnClickListener {
            viewModel.checkLoginStatus()
        }
    }

    override fun onDestroy() {
        webView?.apply {
            if(parent!=null){
                (parent as ViewGroup).removeView(webView)
            }
            stopLoading()
            settings.javaScriptEnabled = false
            clearHistory()
            removeAllViews()
            destroy()
        }

        super.onDestroy()
    }

}