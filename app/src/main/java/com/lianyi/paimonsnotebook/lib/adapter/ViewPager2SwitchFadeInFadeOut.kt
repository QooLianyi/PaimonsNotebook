package com.lianyi.paimonsnotebook.lib.adapter

import android.view.View
import androidx.viewpager2.widget.ViewPager2

class ViewPager2SwitchFadeInFadeOut: ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.alpha = 0f;
        page.visibility = View.VISIBLE;
        //动画切换时间
        page.animate().alpha(1f).duration = 150;
    }
}