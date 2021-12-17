package com.lianyi.paimonsnotebook.ui.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.activity.HoYoLabLoginActivity
import com.lianyi.paimonsnotebook.base.BaseFragment
import com.lianyi.paimonsnotebook.bean.BlackBoardBean
import com.lianyi.paimonsnotebook.bean.DailyNoteBean
import com.lianyi.paimonsnotebook.bean.HomeBannerBean
import com.lianyi.paimonsnotebook.bean.MonthLedgerBean
import com.lianyi.paimonsnotebook.config.*
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.MetaData
import com.lianyi.paimonsnotebook.ui.RefreshData
import com.lianyi.paimonsnotebook.util.*

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

        //nick 点击时的事件
        bind.userNick.setOnClickListener {
            val intent = Intent(bind.root.context,HoYoLabLoginActivity::class.java)
            startActivityForResult(intent,ActivityRequestCode.LOGIN)
        }

        var isOpen = true
            bind.memo.setOnClickListener {
            if(isOpen){
                bind.motion.transitionToEnd()
                bind.memo.transitionToEnd()
            }else{
                bind.motion.transitionToStart()
                bind.memo.transitionToStart()
            }
            isOpen = !isOpen
        }


        bind.dailySign.setOnClickListener {
            showLoading(bind.root.context)
            bind.dailySign.isEnabled = false
        }

        initInformationDetail()
    }

    //树脂详情页和树脂进度条UI更新
    private fun initInformationDetail() {
        activity?.runOnUiThread {
            bind.userNick.text = mainUser?.nickName
            val dailyNoteBean = GSON.fromJson(sp.getString(Settings.SP_DAILY_NOTE_NAME+ mainUser?.gameUid,""),DailyNoteBean::class.java)

            with(bind){
                resinCurrent.text = dailyNoteBean.current_resin.toString()
                resinMax.text = dailyNoteBean.max_resin.toString()
                resinFill.scaleX = dailyNoteBean.current_resin.toFloat() / dailyNoteBean.max_resin

                bind.resinProgressBar.progress = ((dailyNoteBean.current_resin.toFloat()/dailyNoteBean.max_resin.toFloat())*100).toInt()

                resinRecoverTime.text = Format.getResinRecoverTime(dailyNoteBean.resin_recovery_time.toLong())

                dailyTaskFinishedCount.text = dailyNoteBean.finished_task_num.toString()
                dailyTaskMax.text = dailyNoteBean.total_task_num.toString()
                dailyTaskFill.scaleX = dailyNoteBean.finished_task_num.toFloat()/dailyNoteBean.total_task_num

                resinDiscountNum.text = (dailyNoteBean.resin_discount_num_limit - dailyNoteBean.remain_resin_discount_num).toString()
                resinDiscountNumMax.text = dailyNoteBean.resin_discount_num_limit.toString()
                resinDiscountFill.scaleX = (dailyNoteBean.resin_discount_num_limit - dailyNoteBean.remain_resin_discount_num).toFloat() / dailyNoteBean.resin_discount_num_limit.toFloat()

                currentExpeditionNum.text = dailyNoteBean.current_expedition_num.toString()
                maxExpeditionNum.text = dailyNoteBean.max_expedition_num.toString()
                expeditionFill.scaleX = dailyNoteBean.current_expedition_num.toFloat()/dailyNoteBean.max_expedition_num

            }
        }

        //加载本月获得的原石
        RefreshData.getMonthLedger("0", mainUser!!.gameUid,mainUser!!.region){
            activity?.runOnUiThread {
                if(it){
                    val monthLedgerBean = GSON.fromJson(sp.getString(Settings.SP_MONTH_LEDGER_NAME+ mainUser!!.gameUid,""),MonthLedgerBean::class.java)
                    bind.dayGemstone.text = monthLedgerBean.day_data.current_primogems.toString()
                    bind.dayMora.text = monthLedgerBean.day_data.current_mora.toString()
                    bind.monthGemstone.text = monthLedgerBean.month_data.current_primogems.toString()
                    bind.monthMora.text = monthLedgerBean.month_data.current_mora.toString()

                    val colors = listOf(
                        ContextCompat.getColor(bind.root.context,R.color.abyss_fill),
                        ContextCompat.getColor(bind.root.context,R.color.daily_fill),
                        ContextCompat.getColor(bind.root.context,R.color.event_fill),
                        ContextCompat.getColor(bind.root.context,R.color.mail_fill),
                        ContextCompat.getColor(bind.root.context,R.color.adventure_fill),
                        ContextCompat.getColor(bind.root.context,R.color.task_fill),
                        ContextCompat.getColor(bind.root.context,R.color.other_fill)
                    )

                    val pieEntry = mutableListOf<PieEntry>()
                    monthLedgerBean.month_data.group_by.forEach {
                        pieEntry += PieEntry(it.percent.toFloat(),it.action+it.percent+"%")
                    }

                    with(bind.pie){
                        val pieDataSet = PieDataSet(pieEntry,"")
                        pieDataSet.colors =colors
                        pieDataSet.setDrawValues(false)
                        val pieData = PieData(pieDataSet)
                        data = pieData
                        println("加载数据")
                        notifyDataSetChanged()
                    }

                }

                with(bind.pie){
                    isRotationEnabled = false
                    isSelected = false
                    description.isEnabled = false
                    setNoDataText("冒险等级不足,请提升后再来看看吧。")

                    holeRadius = 80f

                    extraRightOffset = 20.dp
                    legend.textSize = 13f

                    legend.orientation = Legend.LegendOrientation.VERTICAL
                    legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                    legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP

                    setTouchEnabled(false)
                    setDrawEntryLabels(false)
                }
            }

        }

    }

    //加载公告页
    private fun loadPagerNotice(pager:View){
        val page = PagerListBinding.bind(pager)
        if(page.list.adapter !=null) return
        MetaData.loadBlackBoardData { result, count ->
            activity?.runOnUiThread {
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

    //加载活动页
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==ActivityResponseCode.OK&&requestCode==ActivityRequestCode.LOGIN){
            initInformationDetail()
        }
    }
}