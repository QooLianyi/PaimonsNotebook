package com.lianyi.paimonsnotebook.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPager2Adapter(val fragments:Map<Int,Fragment>,fragmentManager: FragmentManager, lifecycle: Lifecycle):
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int =
        fragments.size

    override fun createFragment(position: Int): Fragment =
        //根据页面位置返回对应界面
        fragments[position]?: error("请检查fragments数据源和viewpager2的index匹配设置")
}