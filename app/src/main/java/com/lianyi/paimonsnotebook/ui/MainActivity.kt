package com.lianyi.paimonsnotebook.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.edit
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.core.view.get
import com.google.android.material.navigation.NavigationView
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.PaimonsNotebookNoticeBean
import com.lianyi.paimonsnotebook.bean.config.SideBarButtonSettings
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.databinding.ActivityMainBinding
import com.lianyi.paimonsnotebook.databinding.PopNoticeBinding
import com.lianyi.paimonsnotebook.lib.`interface`.BackPressedListener
import com.lianyi.paimonsnotebook.lib.data.UpdateInformation
import com.lianyi.paimonsnotebook.lib.data.UpdateInformation.Companion.getPaimonsNotebookWeb
import com.lianyi.paimonsnotebook.lib.information.AppCenterEvent
import com.lianyi.paimonsnotebook.lib.information.Constants
import com.lianyi.paimonsnotebook.lib.information.PagerIndex
import com.lianyi.paimonsnotebook.ui.activity.FragmentContainerActivity
import com.lianyi.paimonsnotebook.ui.activity.options.OptionsActivity
import com.lianyi.paimonsnotebook.ui.home.*
import com.lianyi.paimonsnotebook.ui.summerland.SummerLandActivity
import com.lianyi.paimonsnotebook.util.*
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.analytics.EventProperties
import org.json.JSONArray
import kotlin.Exception
import kotlin.concurrent.thread

class MainActivity : BaseActivity(){
    private var pressBackTime = 0L

    private val menuTitle = mutableListOf<String>()
    private var menuDetailIsOpen = true

    companion object{
        lateinit var bind: ActivityMainBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initView()
        initClickEvent()
        initConfig()
        //页面切换渐变
//        bind.viewPager2.setPageTransformer(ViewPager2SwitchFadeInFadeOut())
    }

    private fun initView() {
        bind.root.alpha = 0f
        //初始化点击退出的时间
        pressBackTime = System.currentTimeMillis()/1000
        //取消菜单滑动边界
        bind.navView.forEach {
            it.overScrollMode = NavigationView.OVER_SCROLL_NEVER
        }

        //显示主页
        supportFragmentManager.beginTransaction().add(R.id.fragment_container_sub,HomeFragment()).commit()

        bind.navView.setNavigationItemSelectedListener {
            val menuClickEvent = EventProperties()
            val menuSelect = when(it.itemId){
                R.id.menu_daily_materials->{
                    PagerIndex.DAILY_MATERIALS_PAGE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_DAILY_MATERIALS
                }
                R.id.menu_week_materials->{
                    PagerIndex.WEEK_MATERIALS_PAGE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_WEEKLY_MATERIALS
                }
                R.id.menu_character->{
                    PagerIndex.CHARACTER_PAGE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_CHARACTER
                }
                R.id.menu_weapon->{
                    PagerIndex.WEAPON_PAGE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_WEAPON
                }
                R.id.menu_map->{
                    PagerIndex.MAP_PAGE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_MAP
                }
                R.id.menu_wish->{
                    PagerIndex.WISH_PAGE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_WISH
                }
                R.id.menu_search->{
                    PagerIndex.SEARCH_PAGE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_SEARCH
                }
                R.id.menu_hutao_database->{
                    PagerIndex.HUTAO_DATABASE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_HUTAO_DATABASE
                }
//                R.id.menu_cultivate_calculate->{
//                    PagerIndex.CULTIVATE_CALCULATE to AppCenterEvent.MENU_ITEM_SELECT_EVENT_CULTIVATE_CALCULATE
//                }
                else->{
                    PagerIndex.EMPTY_PAGE to AppCenterEvent.EMPTY
                }
            }

            if(it.itemId==R.id.menu_summer_land){
                menuClickEvent.set(AppCenterEvent.MENU_ITEM_SELECT_EVENT_SUMMER_LAND,1)
                goA<SummerLandActivity>()
            }else{
                FragmentContainerActivity.pagerIndex = menuSelect.first
                goA<FragmentContainerActivity>()
                menuClickEvent.set(menuSelect.second,1)
            }

            Analytics.trackEvent(AppCenterEvent.EVENT_MENU_ITEM_SELECT,menuClickEvent)

            //判断是否开启了自动收起侧边栏设置
            if(sp.getBoolean(AppConfig.SP_HOME_SIDE_BAR_AUTO_PICKUP,false)){
                bind.container.closeDrawer(GravityCompat.START)
            }
            true
        }

        bind.navView.menu.forEach {
            menuTitle += it.title.toString()
        }

        setViewMarginBottomByNavigationBarHeight(bind.options)
        setContentMargin(bind.root)
        loadNotice()
//        setDrawerLayoutLeftDragger()
    }

    //初始化各种点击事件
    private fun initClickEvent() {
        //开启侧边栏
        bind.menuSwitch.setOnClickListener {
            bind.container.openDrawer(bind.navViewSpan)
        }
        bind.menuAreaSwitch.setOnClickListener {
            bind.container.openDrawer(bind.navViewSpan)
        }

        //菜单详情切换
        bind.menuDetail.setOnClickListener {
            if(menuDetailIsOpen){
                bind.navView.menu.forEach {
                    it.title = ""
                }
                openAndCloseAnimationHor(bind.navView,AppConfig.MENU_START_WIDTH,AppConfig.MENU_END_WIDTH,200)
            }else{
                menuTitle.forEachIndexed { index, s ->
                    bind.navView.menu[index].title = s
                }
                openAndCloseAnimationHor(bind.navView,AppConfig.MENU_END_WIDTH,AppConfig.MENU_START_WIDTH,200)
            }
            menuDetailIsOpen = !menuDetailIsOpen
        }
        //前往设置
        bind.options.setOnClickListener {
            goA<OptionsActivity>()
            Analytics.trackEvent(AppCenterEvent.EVENT_MENU_ITEM_SELECT,EventProperties().apply {
                set(AppCenterEvent.MENU_ITEM_SELECT_EVENT_OPTIONS,1)
            })
        }
    }

    //设置侧边栏拖动区域
    private fun setDrawerLayoutLeftDragger(){
        try {
            val leftDragger = bind.container.javaClass.getDeclaredField("mLeftDragger").apply {
                isAccessible = true
            }
            val viewDragHelper = leftDragger.get(bind.container)
            val edgeSizeField = viewDragHelper.javaClass.getDeclaredField("mEdgeSize")
            edgeSizeField.apply {
                isAccessible = true
                setInt(viewDragHelper,resources.displayMetrics.widthPixels)
            }

            //取消长按呼出侧边栏
            val leftCallbackField = bind.container.javaClass.getDeclaredField("mLeftCallback").apply {
                isAccessible = true
            }
            val viewDragHelperCallBack = leftCallbackField.get(bind.container)
            viewDragHelperCallBack.javaClass.getDeclaredField("mPeekRunnable").apply {
                isAccessible = true
                set(viewDragHelperCallBack,object :Runnable{
                    override fun run() {
                    }
                })
            }
        }catch (e:Exception){
            e.printStackTrace()
            Log.e("ERROR:", "setDrawerLayoutLeftDragger: 设置侧边栏时出现错误")
        }
    }

    private fun loadNotice(){
        getPaimonsNotebookWeb(Constants.HTML_SELECTOR_PAIMONS_NOTE_BOOK_NOTICE){
            try {
                val noticeList = mutableListOf<PaimonsNotebookNoticeBean>()
                JSONArray(if(it.isEmpty()) "[]" else it).toList(noticeList)
                val noticeBean = noticeList.first()
                val spName = Constants.getNoticeStatus(noticeBean.id)
                if(sp.getBoolean(spName,true)){
                    runOnUiThread {
                        showAlertDialog(bind.root.context,R.layout.pop_notice,false){ it: PopNoticeBinding, win: AlertDialog ->
                            it.close.setOnClickListener {
                                win.dismiss()
                            }
                            it.apply {
                                contentTitle.text = noticeBean.title
                                content.text = noticeBean.content
                                time.text = noticeBean.time
                            }
                        }
                    }
                    sp.edit {
                        putBoolean(spName,false)
                        apply()
                    }
                }
            }catch (e:Exception){
                e.printStackTrace()
                runOnUiThread {
                    "加载公告失败:${e}".showLong()
                }
            }
//            pmc()
//            cnv()
        }
    }

    private fun initConfig() {
        if(SideBarButtonSettings.instance.enabled){
            bind.menuAreaSwitch.layoutParams.apply {
                width = SideBarButtonSettings.instance.sideBarAreaWidth.dp.toInt()
            }
            if(SideBarButtonSettings.instance.hideDefaultSideBarButton){
                bind.menuSwitch.gone()
            }else{
                bind.menuSwitch.show()
            }

            if(SideBarButtonSettings.instance.stylePreview){
                bind.menuAreaSwitch.setBackgroundColor(getColor(R.color.black_alpha_50))
            }else{
                bind.menuAreaSwitch.setBackgroundColor(getColor(R.color.transparent))
            }

        }else{
            bind.menuAreaSwitch.gone()
        }
    }

    private fun pmc(){
        thread {
            getPaimonsNotebookWeb(Constants.ENA){
                val ena = if(it.isEmpty()) false else it.toBoolean()
                sp.edit {
                    putBoolean("ena",ena)
                    apply()
                }
            }
        }
    }

    private fun cnv(){
        getPaimonsNotebookWeb(Constants.HTML_SELECTOR_PAIMONS_NOTE_BOOK_APP_LASTEST_VERSION_CODE){
//            val code = if(it.isEmpty()) 0 else it.toLong()
            val code = 0
            if(code-PaiMonsNoteBook.APP_VERSION_CODE>8){
                showFailureAlertDialog(bind.root.context,getString(R.string.paimonsnotebook_need_update_title),getString(R.string.paimonsnotebook_need_update_context),false)
                thread {
                    Thread.sleep(5000)
                    UpdateInformation.getNewVersionApp()
                    Thread.sleep(5000)
                    error(getString(R.string.paimonsnotebook_need_update_title))
                }
                sp.edit {
                    putBoolean(Constants.SP_NEED_UPDATE,true)
                    apply()
                }
            }
        }
    }

    //当点击返回时
    override fun onBackPressed(){
        if(bind.container.isDrawerOpen(GravityCompat.START)){
            bind.container.closeDrawer(GravityCompat.START)
        }else{
            supportFragmentManager.fragments.forEach {
                if(it is BackPressedListener){
                    if(!it.handelBackPressed()){
                        backPressed()
                    }
                }
            }
        }
    }

    private fun backPressed(){
        val back = System.currentTimeMillis() - pressBackTime
        if(back<AppConfig.PRESS_BACK_INTERVAL){
            finish()
        }else{
            pressBackTime = System.currentTimeMillis()
            "再次点击返回键退出".show()
        }
    }

    //淡入MainActivity
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        bind.root.animate().alpha(1f).duration = 1000
    }

}