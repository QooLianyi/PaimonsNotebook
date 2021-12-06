package com.lianyi.paimonsnotebook

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.lianyi.paimonsnotebook.adapter.MainViewPager2Adapter
import com.lianyi.paimonsnotebook.adapter.NavigationViewSetupWithViewPager2
import com.lianyi.paimonsnotebook.config.PagerIndex
import com.lianyi.paimonsnotebook.databinding.ActivityMainBinding
import com.lianyi.paimonsnotebook.ui.home.*
import com.lianyi.paimonsnotebook.util.gone
import com.lianyi.paimonsnotebook.util.show
import me.jessyan.autosize.internal.CustomAdapt

class MainActivity : AppCompatActivity() ,CustomAdapt{
    lateinit var bind: ActivityMainBinding
    var isOpen = false
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

    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 700f
    }

}