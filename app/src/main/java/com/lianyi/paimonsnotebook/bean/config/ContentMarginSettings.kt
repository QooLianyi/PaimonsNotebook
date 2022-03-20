package com.lianyi.paimonsnotebook.bean.config

import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.util.GSON
import com.lianyi.paimonsnotebook.util.sp

data class ContentMarginSettings(val enable:Boolean,val marginPreview:Boolean,val horizontalProgress:Int,val horizontalMargin:Int,val topProgress:Int,val topMargin:Int) {

    companion object{
        var instance = GSON.fromJson(sp.getString(AppConfig.SP_CONTENT_MARGIN_SETTINGS,""),ContentMarginSettings::class.java)
            ?: ContentMarginSettings(
                enable = false,
                marginPreview = false,
                horizontalProgress = 0,
                horizontalMargin = 0,
                topProgress = 0,
                topMargin = 0
            )
    }
}