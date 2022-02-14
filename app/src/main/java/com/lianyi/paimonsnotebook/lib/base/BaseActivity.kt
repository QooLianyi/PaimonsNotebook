package com.lianyi.paimonsnotebook.lib.base

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.ui.MainActivity
import com.lianyi.paimonsnotebook.util.setContentMargin
import me.jessyan.autosize.internal.CustomAdapt

open class BaseActivity:AppCompatActivity(),CustomAdapt {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //沉浸模式
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //内容扩展显示
            window.apply {
                setDecorFitsSystemWindows(false)
                attributes.apply {
                    layoutInDisplayCutoutMode =
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                }
            }

            window.decorView.windowInsetsController?.let {
                it.hide(WindowInsets.Type.statusBars())
//                it.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                it.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS)
            }

            //设置导航栏
            window.navigationBarColor = getColor(R.color.white_alpha_80)
            window.navigationBarDividerColor = getColor(R.color.black_alpha_10)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        //设置应用只能竖屏显示
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return  AppConfig.AUTO_SIZE
    }
}