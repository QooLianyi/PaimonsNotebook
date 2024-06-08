package com.lianyi.paimonsnotebook.common.database.cultivate.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateItemType

/*
* 养成计划计算项目表(如角色技能,角色实体,武器实体等)
* 用于区分不同养成类型所需的材料,如角色突破与不同技能的养成所需材料区分出来
*
* 使用养成计划表的project_id字段进行绑定
* 使用了外键,删除时一同删除
* */

@Entity(
    "cultivate_items",
    foreignKeys = [
        ForeignKey(
            entity = CultivateEntity::class,
            parentColumns = arrayOf("item_id", "project_id"),
            childColumns = arrayOf("entity_item_id", "project_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    /*
    * 使用计算的项目(当前只有技能)id与计划id以及所属实体id来组合成一个唯一的主键
    * 避免不同计划,相同实体相同技能造成的冲突
    * */
    primaryKeys = [
        "item_id",
        "project_id",
        "entity_item_id"
    ],
    /*
    * 为当前实体表添加一个索引
    * 用于其他表使用当前表作为外键
    * */
    indices = [
        Index(
            "item_id",
            "project_id",
            name = "index_cultivate_materials_fk", //用于materials表的外键约束索引
            unique = true
        )
    ]
)
data class CultivateItems(
    @ColumnInfo("item_id")
    val itemId: Int, //实体id
    @ColumnInfo("entity_item_id")
    val entityItemId: Int, //计算实体id
    @ColumnInfo("project_id")
    val projectId: Int, //计划id
    @ColumnInfo("item_type")
    val itemType: CultivateItemType, //计算的物品类型
    @ColumnInfo("from_level")
    val fromLevel: Int, //起始等级
    @ColumnInfo("to_level")
    val toLevel: Int, //目标等级
    val status: Int //状态
) {
    //1完成,0未完成
    val isFinish
        get() = status == 1
}
