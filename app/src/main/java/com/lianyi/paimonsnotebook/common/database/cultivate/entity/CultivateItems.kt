package com.lianyi.paimonsnotebook.common.database.cultivate.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateItemType

/*
* 养成计划实体表(如角色,武器)
* 使用养成计划表的project_id字段进行绑定
*
* */

@Entity(
    "cultivate_items",
    foreignKeys = [
        ForeignKey(
            entity = CultivateProject::class,
            parentColumns = arrayOf("project_id"),
            childColumns = arrayOf("project_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    primaryKeys = [
        "item_id",
        "project_id"
    ]
)
data class CultivateItems(
    @ColumnInfo("item_id")
    val itemId: Int,
    @ColumnInfo("project_id")
    val projectId: Int,
    @ColumnInfo("item_type")
    val itemType: CultivateItemType,
    @ColumnInfo("from_level")
    val fromLevel: Int,
    @ColumnInfo("to_level")
    val toLevel: Int,
    val status: Int
) {
    //1完成,0未完成
    val isFinish = status == 1
}
