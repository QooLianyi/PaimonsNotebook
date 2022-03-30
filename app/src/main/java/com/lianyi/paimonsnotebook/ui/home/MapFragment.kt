package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import android.view.View
import android.webkit.*
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.databinding.FragmentMapBinding
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.util.mainUser
import com.lianyi.paimonsnotebook.util.setViewMarginBottomByNavigationBarHeight

class MapFragment : BaseFragment(R.layout.fragment_map){
    lateinit var bind: FragmentMapBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentMapBinding.bind(view)

        with(bind.web){
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.domStorageEnabled = true
            settings.blockNetworkImage = false
            settings.useWideViewPort = true

            loadUrl(MiHoYoApi.MAP_V2)

            //设置webView跳转到新页面时在当前页面跳转 而不是创建新的意图
            webViewClient = object : WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    view?.loadUrl(url?:"")
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    MiHoYoApi.getCookie(mainUser!!).split(";")

                    val cookieManager = CookieManager.getInstance()

                    cookieManager.removeSessionCookies(null)

                    cookieManager.setAcceptCookie(true)
                    cookieManager.setCookie(".mihoyo.com", "${Constants.LTOKEN_NAME} = ${mainUser!!.lToken}")
                    cookieManager.setCookie(".mihoyo.com", "${Constants.LTUID_NAME} = ${mainUser!!.loginUid}")
                    cookieManager.flush()

                    //移除顶部二维码
                    loadUrl("javascript:(function() {document.getElementsByClassName('mhy-bbs-app-header')[0].style.display = 'none';})()")
                }

            }

        }

        bind.reload.setOnClickListener {
            bind.web.reload()
        }

        setViewMarginBottomByNavigationBarHeight(bind.web)
    }
}