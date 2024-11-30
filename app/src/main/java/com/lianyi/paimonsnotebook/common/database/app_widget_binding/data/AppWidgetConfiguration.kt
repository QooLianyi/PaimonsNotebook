package com.lianyi.paimonsnotebook.common.database.app_widget_binding.data

import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid
import com.lianyi.paimonsnotebook.ui.widgets.util.enums.AppWidgetBackgroundScaleType
import com.lianyi.paimonsnotebook.ui.widgets.util.enums.AppWidgetTimeFormat

/*
* 桌面组件配置
*
* textColor:字体颜色
* backgroundPattern:背景主题
* bindingGameRole:绑定的角色信息
* imageTintColor:图片颜色
* background:背景信息
* */
data class AppWidgetConfiguration(
    val textColor: Int? = null,
    val bindingGameRole: BindingGameRole? = null,
    val imageTintColor: Int? = null,
    val background: AppWidgetBackground? = null,
    val textFormat: AppWidgetTimeFormat? = null
) {
    data class AppWidgetBackground(
        val backgroundPattern: String? = null,
        val backgroundColor: Int? = null,
        val backgroundRadius: Float? = null,
        val backgroundImage: String? = null,
        val backgroundImageUrl: String? = null,
        val backgroundImageIsUrl: Boolean? = null,
        val backgroundScaleType: AppWidgetBackgroundScaleType? = null
    )

    /*
    * gameRole:服务器与uid
    * gameBiz:游戏id
    * */
    data class BindingGameRole(
        val playerUid: PlayerUid,
        val regionName: String,
        val gameBiz: String,
        val nickname: String
    )
}
