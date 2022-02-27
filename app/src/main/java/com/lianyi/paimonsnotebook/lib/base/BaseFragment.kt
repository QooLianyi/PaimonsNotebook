package com.lianyi.paimonsnotebook.lib.base

import androidx.fragment.app.Fragment
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.lib.`interface`.BackPressedListener
import me.jessyan.autosize.internal.CustomAdapt

open class BaseFragment:Fragment ,CustomAdapt,BackPressedListener{

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    override fun isBaseOnWidth(): Boolean {
        return true
    }

    override fun getSizeInDp(): Float {
        return AppConfig.AUTO_SIZE
    }

    //另一种fragment触发onBackPressed回调的方法
    override fun handelBackPressed(): Boolean {
        return false
    }
}