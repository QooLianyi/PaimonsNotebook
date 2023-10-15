package com.lianyi.paimonsnotebook.ui.screen.setting.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.ui.screen.setting.util.configuration_enum.HomeScreenDisplayState

/*
* 配置信息
* */
class ConfigurationData {
    //首页显示状态
    var homeScreenDisplayState by mutableStateOf(HomeScreenDisplayState.Loading)

    //启用悬浮窗
    var enableOverlay by mutableStateOf(false)

    //总是使用默认用户
    var alwaysUseDefaultUser by mutableStateOf(false)

    //启用自动删除过期图片
    var enableAutoCleanExpiredImages by mutableStateOf(false)
}
