package com.lianyi.paimonsnotebook.ui.activity.home

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.webkit.*
import com.lianyi.paimonsnotebook.bean.ArticleBean
import com.lianyi.paimonsnotebook.databinding.ActivityArticleBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.html.ImageGetter
import com.lianyi.paimonsnotebook.lib.html.TagHandler
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.Format
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.util.*
import com.microsoft.appcenter.AppCenter
import java.lang.Exception
import kotlin.concurrent.thread

class ArticleActivity : BaseActivity() {
    lateinit var bind:ActivityArticleBinding

    companion object{
        lateinit var articleId:String
    }

    private var loading = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
    }

    private fun initView() {
        Ok.get(MiHoYoApi.getArticleDataUrl(articleId), arrayOf("Referer" to "https://bbs.mihoyo.com/")){
            if(it.ok){
                val article = GSON.fromJson(it.optString("data"),ArticleBean::class.java)
                    thread {
                        try {
                            val htmlText = Html.fromHtml(article.post.post.content,Html.FROM_HTML_MODE_LEGACY,ImageGetter(),null)
                            runOnUiThread {
                                bind.content.text = htmlText
                                bind.title.text = article.post.post.subject
                                bind.createTime.text = Format.TIME_HYPHEN_MONTH_DAY.format((article.post.post.created_at.toLong()*1000))
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                            runOnUiThread {
                                "获取文章失败啦".show()
                                finish()
                            }
                        }
                    }
            }else{
                runOnUiThread {
                    bind.web.apply {
                        settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        settings.javaScriptEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.domStorageEnabled = true
                        settings.blockNetworkImage = false
                        settings.useWideViewPort = true
                        webViewClient = object :WebViewClient(){
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)

                                if(progress==100&&loading){
                                    loading = false
                                    bind.web.show()
                                    val cookieManager = CookieManager.getInstance()

                                    cookieManager.removeSessionCookies(null)

                                    cookieManager.setAcceptCookie(true)
                                    cookieManager.setCookie(".mihoyo.com", "${Constants.LTOKEN_NAME} = ${mainUser!!.lToken}")
                                    cookieManager.setCookie(".mihoyo.com", "${Constants.LTUID_NAME} = ${mainUser!!.loginUid}")
                                    cookieManager.setCookie(".mihoyo.com", "${Constants.COOKIE_TOKEN_NAME} = ${mainUser!!.cookieToken}")
                                    cookieManager.setCookie(".mihoyo.com", "${Constants.ACCOUNT_ID_NAME} = ${mainUser!!.loginUid}")
                                    cookieManager.flush()

                                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
                                    "当前加载的内容是网页\n网页内部分功能不可用\n竖屏锁定已解除".show()
                                    thread {
                                        Thread.sleep(500)
                                        runOnUiThread {
                                            //隐藏一般文章界面的logo
                                            loadUrl("javascript:(function() {document.getElementsByClassName('mhy-bbs-app-header')[0].style.display = 'none';})()")
                                            //隐藏特殊界面的logo
                                            loadUrl("javascript:(function() {document.getElementsByClassName('components-common-assets-bbs-header___bbs-header--2rXKS')[0].style.display = 'none';})()")
                                        }
                                    }
                                }

                            }

                            override fun onReceivedHttpError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                errorResponse: WebResourceResponse?
                            ) {
                                super.onReceivedHttpError(view, request, errorResponse)
                                runOnUiThread {
                                    "加载文章失败啦,请重新尝试一下吧!".show()
                                    finish()
                                }
                            }
                        }
                        loadUrl(articleId)
                    }
                }
            }
        }

        setContentMargin(bind.root)
        setViewMarginBottomByNavigationBarHeight(bind.content)
    }
}