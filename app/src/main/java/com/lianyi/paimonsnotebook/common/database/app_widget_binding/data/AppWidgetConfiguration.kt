package com.lianyi.paimonsnotebook.common.database.app_widget_binding.data

import com.lianyi.paimonsnotebook.common.data.hoyolab.PlayerUid

/*
* 小组件配置
*
* textColor:字体颜色
* backgroundPattern:背景主题
* bindingGameRole:绑定的角色信息
* imageTintColor:图片颜色
* */
data class AppWidgetConfiguration(
    val textColor: Int? = null,
    val backgroundPattern: String? = null,
    val bindingGameRole: BindingGameRole? = null,
    val imageTintColor: Int? = null
) {
    /*
    * gameRole:服务器与uid
    * gameBiz:游戏id
    * */
    data class BindingGameRole(
        val playerUid: PlayerUid,
        val regionName:String,
        val gameBiz: String,
        val nickname: String
    )
}
