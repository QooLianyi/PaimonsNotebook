package com.lianyi.paimonsnotebook.common.database.cultivate.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

/*
* 养成计划材料列表
* */
@Entity(
    "cultivate_item_materials",

    )
data class CultivateItemMaterials(
    @ColumnInfo("item_id")
    val itemId: Int,
    @ColumnInfo("cultivate_item_id")
    val cultivateItemId: Int,
    @ColumnInfo("project_id")
    val projectId: Int,
    val count: Int,
    val status: Int
) {
    //1完成,0未完成
    val isFinish
        get() = status == 1
}
