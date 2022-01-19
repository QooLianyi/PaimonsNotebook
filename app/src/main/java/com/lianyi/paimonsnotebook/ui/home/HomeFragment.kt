package com.lianyi.paimonsnotebook.ui.home

import android.app.UiModeManager
import android.content.Context
import android.os.Bundle
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.home.BlackBoardBean
import com.lianyi.paimonsnotebook.bean.home.HomeInformationBean
import com.lianyi.paimonsnotebook.bean.home.HomeOfficialCommendPostBean
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.adapter.HomeBannerAdapter
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.ui.activity.SearchResultActivity
import com.lianyi.paimonsnotebook.ui.activity.home.AccountManagerActivity
import com.lianyi.paimonsnotebook.ui.activity.home.DailySignActivity
import com.lianyi.paimonsnotebook.ui.activity.home.MonthLedgeActivity
import com.lianyi.paimonsnotebook.util.*

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    lateinit var bind:FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentHomeBinding.bind(view)

        Ok.get(MiHoYoApi.HOME_PAGER_INFORMATION){
            if(it.ok){
                val bannerBean = GSON.fromJson(it.optString("data"), HomeInformationBean::class.java)
                bind.homeBanner.apply {
                    adapter = HomeBannerAdapter(bannerBean.carousels)
                    setBannerGalleryMZ(40)
                }
            }
        }

        Ok.get(MiHoYoApi.BLACK_BOARD){
            val blackBoard = GSON.fromJson(it.toString(),BlackBoardBean::class.java)
            val nearActivity = mutableListOf<BlackBoardBean.DataBean.ListBean>()

            blackBoard.data.list.forEach {
                if(it.kind=="1"&&it.break_type=="0"){
                    nearActivity += it
                }
            }
            activity?.runOnUiThread {
                bind.homeNearActivity.adapter = ReAdapter(nearActivity,R.layout.item_home_near_activity){
                        view, listBean, position ->
                    val item = ItemHomeNearActivityBinding.bind(view)
                    loadImage(item.icon,listBean.img_url)
                    item.title.text = listBean.title
                    item.time.text = "${Format.TIME.format(listBean.start_time.toLong()*1000)} - ${Format.TIME.format(listBean.end_time.toLong()*1000)}"
                }
            }
        }

        Ok.get(MiHoYoApi.OFFICIAL_RECOMMEND_POST){
            if(it.ok){
                val recommendPost = GSON.fromJson(it.optString("data"),HomeOfficialCommendPostBean::class.java)
                recommendPost.list.sortByDescending { it.post_id }

                bind.homeNotice.adapter = ReAdapter(recommendPost.list,R.layout.item_home_notice){
                        view, listBean, position ->
                    val item = ItemHomeNoticeBinding.bind(view)
                    loadImage(item.cover,listBean.banner)
                    item.title.text = listBean.subject
                }
            }
        }

        initView()
//        initInformationDetail()
    }


    private fun initView(){
        var isOpen = false
        bind.memo.setOnClickListener {
            if(isOpen){
                bind.motion.transitionToStart()
                bind.memo.transitionToStart()
            }else{
                bind.motion.transitionToEnd()
                bind.memo.transitionToEnd()
            }
            isOpen = !isOpen
        }

        bind.dailySign.setOnClickListener {
            goA<DailySignActivity>()
        }

        bind.accountManager.setOnClickListener {
            goA<AccountManagerActivity>()
        }

        bind.meInformation.setOnClickListener {
            goA<SearchResultActivity>()
        }

        bind.monthLedge.setOnClickListener {
            goA<MonthLedgeActivity>()
            val uiManager = context!!.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager

            if (uiManager.nightMode == UiModeManager.MODE_NIGHT_YES) {
                activity!!.window
                "深色模式"
            } else {
                "非深色模式"
            }.show()
        }
    }

//    private fun initView(){
//        val pages = listOf(
//            layoutInflater.inflate(R.layout.pager_list,null),
//            layoutInflater.inflate(R.layout.pager_list,null)
//        )
//        val titles = listOf(
//            "公告",
//            "活动"
//        )
//
//        bind.homeViewPager.adapter = PagerAdapter(pages,titles)
//
//        bind.homeTabLayout.tab {
//            when(it){
//                0->{
//                    loadPagerNotice(pages[it])
//                }
//                1->{
//                    loadPagerActivity(pages[it])
//                }
//            }
//        }
//
//        bind.homeViewPager.offscreenPageLimit = 3
//        bind.homeTabLayout.setupWithViewPager(bind.homeViewPager)
//
//        //nick 点击时的事件
//        bind.userNick.setOnClickListener {
//            val intent = Intent(bind.root.context,HoYoLabLoginActivity::class.java)
//            startActivityForResult(intent, ActivityRequestCode.LOGIN)
//        }
//
//        var isOpen = true
//        bind.memo.setOnClickListener {
//            if(isOpen){
//                bind.motion.transitionToEnd()
//                bind.memo.transitionToEnd()
//            }else{
//                bind.motion.transitionToStart()
//                bind.memo.transitionToStart()
//            }
//            isOpen = !isOpen
//        }
//
//
//        bind.dailySign.setOnClickListener {
//            goA<DailySignActivity>()
//        }
//    }

    //树脂详情页和树脂进度条UI更新
//    private fun initInformationDetail() {
//        activity?.runOnUiThread {
//            bind.userNick.text = mainUser?.nickName
//            val dailyNoteBean = GSON.fromJson(sp.getString(Constants.SP_DAILY_NOTE_NAME+ mainUser?.gameUid,""),
//                DailyNoteBean::class.java)
//
//            with(bind){
//                resinCurrent.text = dailyNoteBean.current_resin.toString()
//                resinMax.text = dailyNoteBean.max_resin.toString()
//                resinFill.scaleX = dailyNoteBean.current_resin.toFloat() / dailyNoteBean.max_resin
//
//                bind.resinProgressBar.progress = ((dailyNoteBean.current_resin.toFloat()/dailyNoteBean.max_resin.toFloat())*100).toInt()
//
//                resinRecoverTime.text = Format.getResinRecoverTime(dailyNoteBean.resin_recovery_time.toLong())
//
//                dailyTaskFinishedCount.text = dailyNoteBean.finished_task_num.toString()
//                dailyTaskMax.text = dailyNoteBean.total_task_num.toString()
//                dailyTaskFill.scaleX = dailyNoteBean.finished_task_num.toFloat()/dailyNoteBean.total_task_num
//
//                resinDiscountNum.text = (dailyNoteBean.resin_discount_num_limit - dailyNoteBean.remain_resin_discount_num).toString()
//                resinDiscountNumMax.text = dailyNoteBean.resin_discount_num_limit.toString()
//                resinDiscountFill.scaleX = (dailyNoteBean.resin_discount_num_limit - dailyNoteBean.remain_resin_discount_num).toFloat() / dailyNoteBean.resin_discount_num_limit.toFloat()
//
//                currentExpeditionNum.text = dailyNoteBean.current_expedition_num.toString()
//                maxExpeditionNum.text = dailyNoteBean.max_expedition_num.toString()
//                expeditionFill.scaleX = dailyNoteBean.current_expedition_num.toFloat()/dailyNoteBean.max_expedition_num
//
//            }
//        }
//
//        //加载本月获得的原石
//        RefreshData.getMonthLedger("0", mainUser!!.gameUid,mainUser!!.region){
//            activity?.runOnUiThread {
//                if(it){
//                    val monthLedgerBean = GSON.fromJson(sp.getString(Constants.SP_MONTH_LEDGER_NAME+ mainUser!!.gameUid,""),MonthLedgerBean::class.java)
//                    bind.dayGemstone.text = monthLedgerBean.day_data.current_primogems.toString()
//                    bind.dayMora.text = monthLedgerBean.day_data.current_mora.toString()
//                    bind.monthGemstone.text = monthLedgerBean.month_data.current_primogems.toString()
//                    bind.monthMora.text = monthLedgerBean.month_data.current_mora.toString()
//
//
//                    val pieEntry = mutableListOf<PieEntry>()
//                    monthLedgerBean.month_data.group_by.forEach {
//                        pieEntry += PieEntry(it.percent.toFloat(),it.action+it.percent+"%")
//                    }
//
//                    with(bind.pie){
//                        val pieDataSet = PieDataSet(pieEntry,"")
//                        pieDataSet.colors =Constants.monthLegendColors
//                        pieDataSet.setDrawValues(false)
//                        val pieData = PieData(pieDataSet)
//                        data = pieData
//                        println("加载数据")
//                        notifyDataSetChanged()
//                    }
//
//                }
//
//                with(bind.pie){
//                    isRotationEnabled = false
//                    isSelected = false
//                    description.isEnabled = false
//                    setNoDataText("冒险等级不足,请提升后再来看看吧。")
//
//                    holeRadius = 80f
//
//                    extraRightOffset = 20.dp
//                    legend.textSize = 13f
//
//                    legend.orientation = Legend.LegendOrientation.VERTICAL
//                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
//                    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
//
//                    setTouchEnabled(false)
//                    setDrawEntryLabels(false)
//                }
//            }
//
//        }
//
//    }
//
//    //加载公告页
//    private fun loadPagerNotice(pager:View){
//        val page = PagerListBinding.bind(pager)
//        if(page.list.adapter !=null) return
//        MetaData.loadBlackBoardData { result, count ->
//            activity?.runOnUiThread {
//                val bannerData = GSON.fromJson(sp.getString(JsonCacheName.BANNER_CACHE,""),
//                    HomeBannerBean::class.java)
//                bannerData.list.sortByDescending { it.post_id.toLong() }
//                page.list.adapter = ReAdapter(bannerData.list,R.layout.item_home_notice){
//                        view: View, listBean: HomeBannerBean.ListBean, i: Int ->
//                    val item = ItemHomeBannerBinding.bind(view)
//                    item.title.text = listBean.subject
//                    if(listBean.banner.isEmpty()){
//                        item.cover.setImageResource(R.drawable.icon_official_notice)
//                    }else{
//                        loadImage(item.cover,listBean.banner)
//                    }
//                }
//            }
//        }
//    }
//
//    //加载活动页
//    private fun loadPagerActivity(pager:View){
//        val page = PagerListBinding.bind(pager)
//        if(page.list.adapter !=null) return
//            activity?.runOnUiThread {
//                val blackBoard = GSON.fromJson(sp.getString(JsonCacheName.BLACK_BOARD,""),
//                    BlackBoardBean::class.java)
//                if(blackBoard !=null){
//                    val activityList = mutableListOf<BlackBoardBean.DataBean.ListBean>()
//                    blackBoard.data.list.forEach {
//                        if(it.kind=="1"&&it.break_type=="0"){
//                            activityList += it
//                        }
//                    }
//
//                    page.list.adapter = ReAdapter(activityList,R.layout.item_home_activity){
//                            view, listBean, position ->
//                        val item = ItemHomeActivityBinding.bind(view)
//
//                        item.title.text = listBean.title
//                        item.time.text = "${Format.TIME.format(listBean.start_time.toLong()*1000L)} - ${Format.TIME.format(listBean.end_time.toLong()*1000L)}"
//                        loadImage(item.cover,listBean.img_url)
//
//                        val bgColor = if(listBean.padding_color.isNotEmpty()) listBean.padding_color else "#000"
//                        val fontColor = if(listBean.font_color.isNotEmpty()) listBean.font_color else "#000"
//                        item.activitySpan.setBackgroundColor(Color.parseColor(bgColor))
//                        item.title.setTextColor(Color.parseColor(fontColor))
//                        item.time.setTextColor(Color.parseColor(fontColor))
//                        item.root.setOnClickListener {
//                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(listBean.jump_url))
//                            startActivity(intent)
//                        }
//                    }
//                }else{
//                    getString(R.string.error_can_not_get_black_board).show()
//                }
//            }
//        }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode== ActivityResponseCode.OK&&requestCode==ActivityRequestCode.LOGIN){
//            initInformationDetail()
//        }
//    }
}