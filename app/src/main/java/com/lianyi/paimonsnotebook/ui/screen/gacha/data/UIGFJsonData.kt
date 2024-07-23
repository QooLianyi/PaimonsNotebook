package com.lianyi.paimonsnotebook.ui.screen.gacha.data

import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.gacha.entity.GachaItems
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper

data class UIGFJsonData(
    val info: Info,
    val list: List<GachaItems>,
) {
    data class Info(
        val uid: String,
        val lang: String,
        val region_time_zone: Long,
        val export_timestamp: Long = System.currentTimeMillis(),
        val export_time:String = "",
        val export_app: String = PaimonsNotebookApplication.name,
        val export_app_version: String = PaimonsNotebookApplication.version,
        val uigf_version: String = UIGFHelper.UIGF_VERSION,
    )
}
