package com.lianyi.paimonsnotebook.ui.widgets.util

object RemoteViewsTypeHelper {

    fun getTypeNameByType(type: RemoteViewsType) =
        when (type) {
            RemoteViewsType.Util -> "工具"
            RemoteViewsType.GachaHistory -> "祈愿记录"
            RemoteViewsType.DailyNote -> "实时便笺"
            RemoteViewsType.DailyNoteResin -> "实时便笺-树脂"
            RemoteViewsType.DailyNoteHomeIcon -> "实时便笺-洞天宝钱"
            RemoteViewsType.DailyNoteExpedition -> "实时便笺-派遣"
            RemoteViewsType.DailyMaterial -> "每日材料"
            else -> "未分类"
        }

}