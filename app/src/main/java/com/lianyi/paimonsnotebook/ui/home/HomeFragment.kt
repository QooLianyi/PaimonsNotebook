package com.lianyi.paimonsnotebook.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.dailynote.DailyNoteBean
import com.lianyi.paimonsnotebook.bean.home.BlackBoardBean
import com.lianyi.paimonsnotebook.bean.home.HomeInformationBean
import com.lianyi.paimonsnotebook.bean.home.HomeOfficialCommendPostBean
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.adapter.HomeBannerAdapter
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.ui.activity.home.AccountManagerActivity
import com.lianyi.paimonsnotebook.ui.activity.home.DailySignActivity
import com.lianyi.paimonsnotebook.ui.activity.home.MonthLedgerActivity
import com.lianyi.paimonsnotebook.util.*

class HomeFragment : BaseFragment(R.layout.fragment_home) {

    lateinit var bind: FragmentHomeBinding

    private val nearActivity = mutableListOf<BlackBoardBean.DataBean.ListBean>()
    private val bannerData = mutableListOf<HomeInformationBean.CarouselsBean>()
    private val notices = mutableListOf<HomeOfficialCommendPostBean.ListBean>()
    private var dailyNoteIsOpen = false //便笺开关标记

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentHomeBinding.bind(view)

        initView()
    }

    private fun initView() {
        bind.memo.setOnClickListener {
            if (dailyNoteIsOpen) {
                bind.motion.transitionToStart()
                bind.memo.transitionToStart()
            } else {
                bind.motion.transitionToEnd()
                bind.memo.transitionToEnd()
            }
            dailyNoteIsOpen = !dailyNoteIsOpen
        }

        bind.dailySign.setOnClickListener {
            goA<DailySignActivity>()
        }

        bind.accountManager.setOnClickListener {
            startActivityForResult(Intent(activity!!,AccountManagerActivity::class.java),ActivityRequestCode.ACCOUNT_MANAGER)
        }

        bind.meInformation.setOnClickListener {
            bind.meInformation.isEnabled = false
            SearchFragment.querySelfInformation(mainUser!!.gameUid) {
                activity?.runOnUiThread {
                    bind.meInformation.isEnabled = true
                }
            }
        }

        bind.monthLedge.setOnClickListener {
            goA<MonthLedgerActivity>()
//            val uiManager = context!!.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
//
//            if (uiManager.nightMode == UiModeManager.MODE_NIGHT_YES) {
//                activity!!.window
//                "深色模式"
//            } else {
//                "非深色模式"
//            }.show()
        }
        bind.swipe.setColorSchemeColors(
            ContextCompat.getColor(
                bind.root.context,
                R.color.wave_ocean_blue_01
            )
        )
        bind.swipe.setOnRefreshListener {
            refreshData()
            bind.swipe.isRefreshing = false
        }

        //近期活动
        bind.homeNearActivity.adapter =
            ReAdapter(nearActivity, R.layout.item_home_near_activity) { view, listBean, position ->
                val item = ItemHomeNearActivityBinding.bind(view)
                loadImage(item.icon, listBean.img_url)
                item.title.text = listBean.title
                item.time.text = "${Format.TIME.format(listBean.start_time.toLong() * 1000)} - ${
                    Format.TIME.format(listBean.end_time.toLong() * 1000)
                }"
            }

        //公告
        bind.homeNotice.adapter =
            ReAdapter(notices, R.layout.item_home_notice) { view, listBean, position ->
                val item = ItemHomeNoticeBinding.bind(view)
                loadHomeNoticeImage(item.cover, listBean.banner)
                item.title.text = listBean.subject
            }

        //展开时收起
//        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                if (dailyNoteIsOpen) {
//                    bind.memo.transitionToStart()
//                    bind.motion.transitionToStart()
//                    dailyNoteIsOpen = false
//                } else {
//                }
//            }
//        })

        setViewMarginBottomByNavigationBarHeight(bind.homeNotice,bind.memoAnchor)
        refreshData()
    }

    private fun refreshData() {
        //banner
        Ok.get(MiHoYoApi.HOME_PAGER_INFORMATION) {
            if (it.ok) {
                val bannerBean =
                    GSON.fromJson(it.optString("data"), HomeInformationBean::class.java)
                bannerBean.carousels.copy(bannerData)
                activity?.runOnUiThread {
                    //banner
                    bind.homeBanner.apply {
                        adapter = HomeBannerAdapter(bannerData)
                        setBannerGalleryMZ(40)
                    }
                }
            }
        }

        //近期活动
        Ok.get(MiHoYoApi.BLACK_BOARD) {
            if (it.ok) {
                val blackBoard = GSON.fromJson(it.toString(), BlackBoardBean::class.java)
                nearActivity.clear()
                blackBoard.data.list.forEach {
                    if (it.kind == "1" && it.break_type == "0") {
                        nearActivity += it
                    }
                }
                activity?.runOnUiThread {
                    bind.homeNearActivity.adapter?.notifyDataSetChanged()
                }
            }
        }

        //公告
        Ok.get(MiHoYoApi.OFFICIAL_RECOMMEND_POST) {
            if (it.ok) {
                val recommendPost =
                    GSON.fromJson(it.optString("data"), HomeOfficialCommendPostBean::class.java)
                recommendPost.list.sortByDescending { it.post_id }
                recommendPost.list.copy(notices)
                activity?.runOnUiThread {
                    bind.homeNotice.adapter?.notifyDataSetChanged()
                }
            }
        }
        dailyNoteRefesh()
    }
    //树脂详情页和树脂进度条UI更新
    private fun dailyNoteRefesh() {
        Ok.get(MiHoYoApi.getDailyNoteUrl(mainUser!!.gameUid, mainUser!!.region)){
            if(it.ok){
                val dailyNoteBean = GSON.fromJson(it.optString("data"),DailyNoteBean::class.java)
                activity?.runOnUiThread {
                    with(bind.informationDetailSpan) {
                        //外部树脂进度条
                        bind.resinProgressBar.progress =
                            ((dailyNoteBean.current_resin.toFloat() / dailyNoteBean.max_resin.toFloat()) * 100).toInt()

                        //树脂
                        resinCurrent.text = dailyNoteBean.current_resin.toString()
                        resinMax.text = dailyNoteBean.max_resin.toString()
                        resinRecoverTime.text =
                            Format.getRecoverTimeDayText(dailyNoteBean.resin_recovery_time.toLong())

                        //每日任务
                        dailyTaskFinishedCount.text = dailyNoteBean.finished_task_num.toString()
                        dailyTaskMax.text = dailyNoteBean.total_task_num.toString()

                        if(dailyNoteBean.isIs_extra_task_reward_received){
                            dailyTaskState.text = Constants.DAILY_TASK_STATE_FINISHED
                        }else{
                            dailyTaskState.text = Constants.DAILY_TASK_STATE_NOT_FINISHED
                        }

                        //洞天宝钱
                        homeIconCurrent.text = dailyNoteBean.current_home_coin.toString()
                        homeIconMax.text = dailyNoteBean.max_home_coin.toString()
                        homeIconTime.text = Format.getRecoverTimeHourText(dailyNoteBean.home_coin_recovery_time.toLong())

                        //周本
                        resinDiscountNum.text =
                            (dailyNoteBean.resin_discount_num_limit - dailyNoteBean.remain_resin_discount_num).toString()
                        resinDiscountNumMax.text = dailyNoteBean.resin_discount_num_limit.toString()

                        //派遣
                        currentExpeditionNum.text = dailyNoteBean.current_expedition_num.toString()
                        maxExpeditionNum.text = dailyNoteBean.max_expedition_num.toString()
                        expeditionsList.adapter = ReAdapter(dailyNoteBean.expeditions,R.layout.item_expeditions){
                            view, expeditionsBean, position ->
                            val item = ItemExpeditionsBinding.bind(view)
                            loadImage(item.icon,expeditionsBean.avatar_side_icon)
                            val leftTime = expeditionsBean.remained_time.toLong()
                            if(leftTime>0){
                                item.time.text = Format.getRecoverTimeHourText(leftTime)
                                item.leftTimeText.show()
                                item.time.setTextColor(ContextCompat.getColor(activity!!,R.color.the_expedition_circular_ring))
                                item.circularRing.setColorFilter(ContextCompat.getColor(activity!!,R.color.the_expedition_circular_ring))
                            }else{
                                item.leftTimeText.gone()
                                item.time.text = "探险完成"
                                item.time.setTextColor(ContextCompat.getColor(activity!!,R.color.expedition_finished))
                                item.circularRing.setColorFilter(ContextCompat.getColor(activity!!,R.color.expedition_finished))
                            }
                        }
                        //禁用滑动点击
                        expeditionsList.suppressLayout(true)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            ActivityRequestCode.ACCOUNT_MANAGER->{
                //更改默认账号 刷新树脂信息
                if(resultCode==ActivityResponseCode.DATA_CHANGE){
                    dailyNoteRefesh()
                }
            }
        }
    }

    override fun handelBackPressed(): Boolean {
        return if(dailyNoteIsOpen){
            bind.memo.transitionToStart()
            bind.motion.transitionToStart()
            dailyNoteIsOpen = false
            true
        }else{
            false
        }
    }
}
