package com.lianyi.paimonsnotebook.common.extension.activity

import android.app.Activity
import androidx.core.view.WindowCompat


fun Activity.setSystemBarAppearance(boolean: Boolean) {
    WindowCompat.getInsetsController(window, this.window.decorView).apply {
        //设置状态栏与底部导航栏的显示模式(Light/Night) true为黑色
        isAppearanceLightNavigationBars = boolean
        isAppearanceLightStatusBars = boolean
    }
}