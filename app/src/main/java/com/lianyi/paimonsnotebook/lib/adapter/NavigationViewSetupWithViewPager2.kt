package com.lianyi.paimonsnotebook.lib.adapter

import android.view.MenuItem
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView

class NavigationViewSetupWithViewPager2(val vp2: ViewPager2, val nv: NavigationView, val block:((NavigationView, ViewPager2)->Unit)? =null) {
    val map = mutableMapOf<MenuItem,Int>()
    init {
        nv.menu.forEachIndexed { index, item ->
            map[item] = index
        }
    }
    var currentCheck = nv.menu[0]

    fun attach(){
        //block可空 必须用invoke
        block?.invoke(nv,vp2)

        //监听viewpager页面改变 本项目关闭了ViewPager2的滑动操作

//        vp2.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
//            override fun onPageSelected(position: Int) {
//                super.onPageSelected(position)
//                //通过position 设置菜单的选择项
//                nv.setCheckedItem(nv.menu[position])
//            }
//        })
        //添加menu选择监听
        currentCheck.isChecked = true
        nv.setNavigationItemSelectedListener {
            //设置Menu当前选择item的样式
            currentCheck.isChecked = false
            currentCheck = nv.menu[map[it]!!]
            currentCheck.isChecked = true

            vp2.setCurrentItem(map[it] ?:error("NavigationView的item${it}没有对应元素"),false)
            true
        }


    }

}