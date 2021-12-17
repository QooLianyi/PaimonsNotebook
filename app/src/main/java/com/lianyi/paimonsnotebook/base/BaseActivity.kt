package com.lianyi.paimonsnotebook.base

import androidx.appcompat.app.AppCompatActivity
import com.lianyi.paimonsnotebook.config.AppConfig
import me.jessyan.autosize.internal.CustomAdapt

open class BaseActivity:AppCompatActivity(),CustomAdapt {

    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return  AppConfig.AUTO_SIZE
    }
}