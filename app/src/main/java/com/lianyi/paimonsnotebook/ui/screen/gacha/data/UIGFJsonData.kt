package com.lianyi.paimonsnotebook.ui.screen.gacha.data

import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper

data class UIGFJsonData(
    val info: Info,
    val list: List<GachaItems>,
) {
    data class Info(
        val uid: String,
        val lang: String,
        val region_time_zone: Long,
        val export_timestamp: Long,
        val export_time: String,
        val export_app: String,
        val export_app_version: String,
        val uigf_version: String
    ) {
        fun getPropertyList() = listOf(
            "记录UID" to uid,
            "记录来源" to export_app,
            "UIGF版本" to uigf_version,
            "导出时间" to if (export_timestamp != 0L) TimeHelper.getTime(
                export_timestamp
            ) else export_time,
            "导出程序版本" to export_app_version,
            "记录语言" to lang,
            "时区" to "$region_time_zone",
        )
    }
}
