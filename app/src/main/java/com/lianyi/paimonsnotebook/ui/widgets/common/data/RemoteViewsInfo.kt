package com.lianyi.paimonsnotebook.ui.widgets.common.data

import com.lianyi.paimonsnotebook.ui.widgets.core.BaseAppWidget
import com.lianyi.paimonsnotebook.ui.widgets.core.BaseRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetConfigurationOption
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsDataType
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsType

/*
* 远端视图信息
* appWidgetClass:所需的桌面组件
* remoteViewsClass:所需的视图
* dataType:所需的数据类型
* remoteViewsName:远端视图名称
* configurationOptions:显示的配置选项
* */
data class RemoteViewsInfo(
    val appWidgetClass: Class<out BaseAppWidget>,
    val remoteViewsClass: Class<out BaseRemoteViews>,
    val configurationOptions: Set<AppWidgetConfigurationOption>,
    val remoteViewsName: String = "默认名称",
    val dataType: Set<RemoteViewsDataType> = setOf(),
    val remoteViewsType: RemoteViewsType = RemoteViewsType.Default
) {
    companion object {
        //远端视图默认配置选项
        private val defaultConfigurationOptions by lazy {
            setOf(
                AppWidgetConfigurationOption.GameRole,
                AppWidgetConfigurationOption.ChangeWidget,
                AppWidgetConfigurationOption.BackgroundPatten,
                AppWidgetConfigurationOption.TextColor
            )
        }
    }

    //描述
    val description by lazy {
        val list = mutableListOf<String>().apply {

            if(dataType.contains(RemoteViewsDataType.NoNetworkUpdate)){
                this += "更新时无需网络"
            }else{
                this += "更新时需要网络"
            }

            if (dataType.contains(RemoteViewsDataType.DailyNote)) {
                this += "更新时可能需要进行验证"
            }
            if (configurationOptions.contains(AppWidgetConfigurationOption.User)) {
                this += "此组件更新时只会更新用户账号下默认角色的信息"
            }
        }
        if (list.isNotEmpty()) {
            list.joinToString(",")
        } else {
            "此组件没有特殊的要求"
        }
    }

    //尺寸
    val size by lazy {
        val regex = Regex("[0-9]+\\*+[0-9]")
        regex.find(remoteViewsName)?.value ?: "1*1"
    }

}
