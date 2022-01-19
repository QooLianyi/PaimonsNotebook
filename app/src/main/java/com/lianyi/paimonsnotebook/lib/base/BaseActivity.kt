package com.lianyi.paimonsnotebook.lib.base

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.lianyi.paimonsnotebook.config.AppConfig
import me.jessyan.autosize.internal.CustomAdapt

open class BaseActivity:AppCompatActivity(),CustomAdapt {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //隐藏状态栏
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//            window.apply {
//                setDecorFitsSystemWindows(false)
//                attributes.apply {
//                    layoutInDisplayCutoutMode =
//                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT
//                }
//            }
//            window.insetsController?.let {
//                it.hide(WindowInsets.Type.statusBars())
//                it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
//        } else {
//            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        }
    }

    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return  AppConfig.AUTO_SIZE
    }
}