package com.lianyi.paimonsnotebook.ui.screen.gacha.data

import com.google.gson.annotations.SerializedName
import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper

data class UIGFJsonV4Data(
    val info: Info,
    val hk4e: List<HK4E>
) {
    data class Info(
        //此字段为number,string中的一种,此处都按照字符串来处理
        //秒级
        @SerializedName("export_timestamp")
        val exportTimestamp: String,
        @SerializedName("export_app")
        val exportApp: String,
        @SerializedName("export_app_version")
        val exportAppVersion: String,
        val version: String
    ) {
        //记录中的uid列表
        val uidList by lazy {
            mutableListOf<String>()
        }

        fun getPropertyList() =
            listOf(
                "记录来源" to exportApp,
                "UIGF版本" to version,
                "导出时间" to TimeHelper.getTime(
                    exportTimestamp.toLongOrNull() ?: 0L
                ),
                "导出程序版本" to exportAppVersion,
                "包含的UID" to uidList.joinToString(
                    separator = "\n"
                )
            )

    }

    data class HK4E(
        val uid: String,
        val timezone: Int,
        val lang: String,
        val list: List<GachaItems>
    )
}
