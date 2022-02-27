package com.lianyi.paimonsnotebook.lib.`interface`

//用于fragment拦截onBackPressed回调的接口
interface BackPressedListener {
    fun handelBackPressed():Boolean
}