package com.lianyi.paimonsnotebook.ui.screen.home.data

import android.app.Activity
import com.lianyi.paimonsnotebook.ui.screen.home.util.FunctionPages

data class FunctionItemData(
    val name: String,
    val icon: Int,
    val target: FunctionPages,
    val offline: Boolean = false,
    val shortcut: Boolean = false,
)

/*
* 主页侧边栏选项数据
*
* name:功能名称
* icon:图标资源ID
* target:目标activity
* sortIndex:排序类型,升序
* requireMetadata:是否需要metadata
* */
data class ModalItemData(
    val name: String,
    val icon: Int,
    val target: Class<out Activity>,
    val sortIndex: Int = 0,
    val requireMetadata: Boolean = false
)