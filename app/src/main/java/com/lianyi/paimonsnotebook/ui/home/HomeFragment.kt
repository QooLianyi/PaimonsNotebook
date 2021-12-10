package com.lianyi.paimonsnotebook.ui.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.base.BaseFragment
import com.lianyi.paimonsnotebook.bean.BlackBoardBean
import com.lianyi.paimonsnotebook.bean.HomeBannerBean
import com.lianyi.paimonsnotebook.config.Format
import com.lianyi.paimonsnotebook.config.JsonCacheName
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.MetaData
import com.lianyi.paimonsnotebook.ui.RefreshData
import com.lianyi.paimonsnotebook.util.*
import kotlin.concurrent.thread

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    lateinit var bind:FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentHomeBinding.bind(view)

        val pages = listOf(
            layoutInflater.inflate(R.layout.pager_list,null),
            layoutInflater.inflate(R.layout.pager_list,null)
        )
        val titles = listOf(
            "公告",
            "活动"
        )

        bind.homeViewPager.adapter = PagerAdapter(pages,titles)

        bind.homeTabLayout.tab {
            when(it){
                0->{
                    loadPagerNotice(pages[it])
                }
                1->{
                    loadPagerActivity(pages[it])
                }
            }
        }

        bind.homeViewPager.offscreenPageLimit = 3
        bind.homeTabLayout.setupWithViewPager(bind.homeViewPager)

        loadPagerNotice(pages[0])

        bind.userNick.setOnClickListener {
            showLoading(context!!)
            var closeCountDown = 60

            thread {
                while (true){
                    Thread.sleep(1000)
                    closeCountDown--
                    if(closeCountDown<=0){
                        loadingWindowDismiss()
                        return@thread
                    }else{
                        println("距离窗口关闭还有 $closeCountDown 秒")
                    }
                }
            }
        }
    }

    private fun loadPagerNotice(pager:View){
        val page = PagerListBinding.bind(pager)
        if(page.list.adapter !=null) return
        RefreshData.getBanner {
            MetaData.loadDataList { result, count ->
                activity?.runOnUiThread {
                    if(it){
                        val bannerData = GSON.fromJson(sp.getString(JsonCacheName.BANNER_CACHE,""),HomeBannerBean::class.java)
                        bannerData.data.list.sortByDescending { it.post_id.toLong() }
                        page.list.adapter = ReAdapter(bannerData.data.list,R.layout.item_home_banner){
                            view: View, listBean: HomeBannerBean.DataBean.ListBean, i: Int ->
                            val item = ItemHomeBannerBinding.bind(view)
                            item.title.text = listBean.subject
                            if(listBean.banner.isEmpty()){
                                item.cover.setImageResource(R.drawable.icon_official_notice)
                            }else{
                                loadImage(item.cover,listBean.banner)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun loadPagerActivity(pager:View){
        val page = PagerListBinding.bind(pager)
        if(page.list.adapter !=null) return
            activity?.runOnUiThread {
                val blackBoard = GSON.fromJson(sp.getString(JsonCacheName.BLACK_BOARD,""),BlackBoardBean::class.java)
                if(blackBoard !=null){
                    val activityList = mutableListOf<BlackBoardBean.DataBean.ListBean>()
                    blackBoard.data.list.forEach {
                        if(it.kind=="1"&&it.break_type=="0"){
                            activityList += it
                        }
                    }

                    page.list.adapter = ReAdapter(activityList,R.layout.item_home_activity){
                            view, listBean, position ->
                        val item = ItemHomeActivityBinding.bind(view)

                        item.title.text = listBean.title
                        item.time.text = "${Format.TIME.format(listBean.start_time.toLong()*1000L)} - ${Format.TIME.format(listBean.end_time.toLong()*1000L)}"
                        loadImage(item.cover,listBean.img_url)

                        val bgColor = if(listBean.padding_color.isNotEmpty()) listBean.padding_color else "#000"
                        val fontColor = if(listBean.font_color.isNotEmpty()) listBean.font_color else "#000"
                        item.activitySpan.setBackgroundColor(Color.parseColor(bgColor))
                        item.title.setTextColor(Color.parseColor(fontColor))
                        item.time.setTextColor(Color.parseColor(fontColor))
                        item.root.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(listBean.jump_url))
                            startActivity(intent)
                        }
                    }
                }else{
                    getString(R.string.error_can_not_get_black_board).show()
                }
            }
        }

    }