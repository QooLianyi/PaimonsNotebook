package com.lianyi.paimonsnotebook.bean.config

import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.util.GSON
import com.lianyi.paimonsnotebook.util.sp


class SideBarButtonSettings(val enabled:Boolean,val stylePreview:Boolean,val hideDefaultSideBarButton:Boolean,val widthProgress:Int,val sideBarAreaWidth:Int) {
    companion object{
        var instance: SideBarButtonSettings =
            GSON.fromJson(sp.getString(AppConfig.SP_SIDE_BAR_BUTTON_SETTINGS,""),SideBarButtonSettings::class.java)
                ?: SideBarButtonSettings(
                    enabled = true,
                    stylePreview = false,
                    hideDefaultSideBarButton = false,
                    widthProgress = 30,
                    sideBarAreaWidth = 30
                )
    }
    override fun toString(): String {
        return "SideBarButtonSettings(enabled=$enabled, stylePreview=$stylePreview, hideDefaultSideBarButton=$hideDefaultSideBarButton, widthProgress=$widthProgress, sideBarAreaWidth=$sideBarAreaWidth)"
    }
}