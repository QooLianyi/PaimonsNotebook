package com.lianyi.paimonsnotebook.ui.screen.setting.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.ui.screen.setting.util.configuration_enum.HomeScreenDisplayState

/*
* 配置信息
* */
class ConfigurationData{
    //首页显示状态
    var homeScreenDisplayState by mutableStateOf(HomeScreenDisplayState.Loading)
    //是否启用主页模态点击后自动关闭
    var enableHomeModalSelectClose by mutableStateOf(false)
    //启用悬浮窗
    var enableOverlay by mutableStateOf(false)
    //小组件总是选择默认用户的角色
    var appwidgetAlwaysUseSelectedUser by mutableStateOf(false)
}
