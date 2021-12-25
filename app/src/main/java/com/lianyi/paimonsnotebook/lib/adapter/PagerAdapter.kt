package com.lianyi.paimonsnotebook.lib.adapter

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

//viewpager通用适配器
class PagerAdapter(val pages:List<View>, val titles:List<String>): PagerAdapter(){
    override fun getCount(): Int {
        return pages.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        container.addView(pages[position])
        return pages[position]
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(pages[position])
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}