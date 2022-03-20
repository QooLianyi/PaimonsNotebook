package com.lianyi.paimonsnotebook.ui.activity.home

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.home.AnnouncementBean
import com.lianyi.paimonsnotebook.bean.home.AnnouncementListBean
import com.lianyi.paimonsnotebook.databinding.ActivityAnnouncementBinding
import com.lianyi.paimonsnotebook.databinding.ItemHomeNoticeBinding
import com.lianyi.paimonsnotebook.databinding.PagerListBinding
import com.lianyi.paimonsnotebook.lib.adapter.PagerAdapter
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.AppCenterEvent
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.util.*
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.analytics.EventProperties

class AnnouncementActivity : BaseActivity() {
    lateinit var bind:ActivityAnnouncementBinding

    private val announcementContentMap = mutableMapOf<Int,AnnouncementBean.ListBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityAnnouncementBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
    }

    private fun initView() {
        bind.announcementViewPager.offscreenPageLimit = 3
        val titles = listOf("活动公告","游戏公告")
        val pages = listOf(
            layoutInflater.inflate(R.layout.pager_list,null),
            layoutInflater.inflate(R.layout.pager_list,null)
        )

        Ok.get(MiHoYoApi.ANNOUNCEMENT_LIST_URL){ announcementListJson->
            if(announcementListJson.ok){
                Ok.get(MiHoYoApi.ANNOUNCEMENT_URL){ announcementContentJson->
                    runOnUiThread {
                        if(announcementContentJson.ok){
                            val announcementListBean = GSON.fromJson(announcementListJson.optString("data"),AnnouncementListBean::class.java)
                            val announcementList = mutableListOf<AnnouncementListBean.ListBeanX.ListBean>()

                            val announcementContent = GSON.fromJson(announcementContentJson.optString("data"),AnnouncementBean::class.java)
                            announcementContent.list.forEach {
                                announcementContentMap += it.ann_id to it
                            }

                            //获取内容
                            announcementListBean.list.forEach {listBeanX->
                                listBeanX.list.forEach { listBean->
                                    announcementList += listBean
                                }
                            }
                            announcementListBean.type_list.sortBy { it.id }
                            val announcementGroup = announcementList.groupBy { it.type }

                            val type1 = announcementGroup[announcementListBean.type_list.first().id]
                            val type2 = announcementGroup[announcementListBean.type_list.last().id]

                            bind.announcementViewPager.adapter = PagerAdapter(pages,titles)
                            bind.announcementTabLayout.setupWithViewPager(bind.announcementViewPager)

                            if(!type1.isNullOrEmpty()){
                                loadList(pages[0],type1)
                            }

                            if(!type2.isNullOrEmpty()){
                                loadList(pages[1],type2)
                            }
                        }else{
                            "获取公告内容失败啦:${announcementContentJson.optString("retcode")}".show()
                            finish()
                        }
                    }
                }
            }else{
                runOnUiThread {
                    "获取公告列表失败啦:${announcementListJson.optString("retcode")}".show()
                    finish()
                }
            }
        }
        setContentView(bind.root)
    }

    private fun loadList(listView: View, data:List<AnnouncementListBean.ListBeanX.ListBean>){
        val page = PagerListBinding.bind(listView)
        if(page.list.adapter==null){
            page.list.adapter = ReAdapter(data,R.layout.item_home_notice){
                view, listBean, position ->
                val item = ItemHomeNoticeBinding.bind(view)
                loadHomeNoticeImage(item.cover,listBean.banner)
                item.title.text = listBean.title
                item.root.setOnClickListener {
                    val announcementContent = announcementContentMap[listBean.ann_id]
                    if(announcementContent!=null){
                        AnnouncementDetailActivity.announcementContent = announcementContent
                        goA<AnnouncementDetailActivity>()
                        sendEvent(AppCenterEvent.HOME_PAGE_ACTION_ANNOUNCEMENT)
                    }else{
                        "跳转详情失败:没有找到相同的id".show()
                    }
                }
            }
            page.list.setPadding(10.dp.toInt(),0,10.dp.toInt(),0)
            setViewMarginBottomByNavigationBarHeight(page.list)
        }
    }

    private fun sendEvent(event: String) {
        val properties = EventProperties()
        properties.set(event,1)
        Analytics.trackEvent(AppCenterEvent.EVENT_HOME_PAGE_ACTION,properties)
    }
}