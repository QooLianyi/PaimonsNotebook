package com.lianyi.paimonsnotebook.base

import androidx.fragment.app.Fragment
import com.lianyi.paimonsnotebook.config.AppConfig
import me.jessyan.autosize.internal.CustomAdapt

open class BaseFragment:Fragment ,CustomAdapt{

    constructor() : super()
    constructor(contentLayoutId: Int) : super(contentLayoutId)

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return AppConfig.AUTO_SIZE
    }
}