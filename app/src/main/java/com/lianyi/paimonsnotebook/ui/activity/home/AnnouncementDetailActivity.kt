package com.lianyi.paimonsnotebook.ui.activity.home

import android.os.Bundle
import android.text.Html
import com.lianyi.paimonsnotebook.bean.home.AnnouncementBean
import com.lianyi.paimonsnotebook.databinding.ActivityAnnouncementDetailBinding
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.html.ImageGetter
import com.lianyi.paimonsnotebook.util.setViewMarginBottomByNavigationBarHeight
import kotlin.concurrent.thread

class AnnouncementDetailActivity : BaseActivity() {
    lateinit var bind:ActivityAnnouncementDetailBinding

    companion object{
        lateinit var announcementContent:AnnouncementBean.ListBean
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityAnnouncementDetailBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
    }

    private fun initView() {
        thread {
            val contentText = announcementContent.content.replace("&lt;t class=\"t_lc\"&gt;","").replace("&lt;/t&gt;","").replace("&lt;t class=\"t_gl\"&gt;","")
            val content = Html.fromHtml(contentText,Html.FROM_HTML_MODE_LEGACY,ImageGetter(),null)

            runOnUiThread {
                bind.content.text = content
//                bind.subtitle.text = announcementContent.subtitle
                bind.title.text = announcementContent.title
            }
        }
        setViewMarginBottomByNavigationBarHeight(bind.root)
    }
}