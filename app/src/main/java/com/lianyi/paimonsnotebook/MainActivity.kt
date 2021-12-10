package com.lianyi.paimonsnotebook

import android.os.Bundle
import android.view.WindowManager
import com.lianyi.paimonsnotebook.adapter.MainViewPager2Adapter
import com.lianyi.paimonsnotebook.adapter.NavigationViewSetupWithViewPager2
import com.lianyi.paimonsnotebook.base.BaseActivity
import com.lianyi.paimonsnotebook.config.PagerIndex
import com.lianyi.paimonsnotebook.databinding.ActivityMainBinding
import com.lianyi.paimonsnotebook.ui.home.*

class MainActivity : BaseActivity(){
    lateinit var bind: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        //设置需要加载的界面
        val fragments = mapOf(
            PagerIndex.HOME_PAGE to HomeFragment(),
            PagerIndex.DAILY_MATERIALS_PAGE to DailyMaterialsFragment(),
            PagerIndex.WEEK_MATERIALS_PAGE to WeekMaterialsFragment(),
            PagerIndex.CHARACTER_PAGE to CharacterFragment(),
            PagerIndex.WEAPON_PAGE to WeaponFragment(),
            PagerIndex.MAP_PAGE to MapFragment(),
            PagerIndex.WISH_PAGE to WishFragment(),
            PagerIndex.SEARCH_PAGE to SearchFragment(),
            PagerIndex.TIME_LINE_PAGE to TimeLineFragment(),
        )

        bind.menuSwitch.setOnClickListener {
            bind.container.openDrawer(bind.navView)
        }

        //设置页面缓存数
        bind.viewPager2.offscreenPageLimit = 8
        bind.viewPager2.adapter = MainViewPager2Adapter(fragments,supportFragmentManager,lifecycle)
        NavigationViewSetupWithViewPager2(bind.viewPager2,bind.navView){
            navigationView, viewPager2 ->
            //关闭ViewPager2滑动
            viewPager2.isUserInputEnabled = false
        }.attach()

//        bind.viewPager2.setPageTransformer(ViewPager2SwitchFadeInFadeOut())


    }

}