package com.lianyi.paimonsnotebook.common.database.cultivate.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateEntityType

/*
* 养成计划实体表(如角色,武器)
* 使用养成计划表的project_id字段进行绑定
*
* */

@Entity(
    "cultivate_entity",
    foreignKeys = [
        ForeignKey(
            entity = CultivateProject::class,
            parentColumns = arrayOf("project_id"),
            childColumns = arrayOf("project_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    /*
    * 使用计算的item_id与计划id组合成一个唯一的主键
    * 避免不同计划相同id实体冲突
    * */
    primaryKeys = [
        "item_id",
        "project_id"
    ],
    /*
    * 为当前实体表添加一个索引
    * 用于其他表使用当前表作为外键
    * */
    indices = [
        Index(
            "item_id",
            "project_id",
            name = "index_cultivate_items_fk", //用于items表的外键约束索引
            unique = true
        )
    ]
)
data class CultivateEntity(
    @ColumnInfo("item_id")
    val itemId: Int, //实体id,如角色id
    @ColumnInfo("project_id")
    val projectId: Int, //计划id
    val type: CultivateEntityType, //实体类型
    @ColumnInfo("bind_entity_id")
    val bindEntityId: Int = -1, //绑定的itemId,此值不设置默认为-1,代表没有绑定对象
    val status: Int, //状态
    @ColumnInfo("add_time")
    val addTime: Long = System.currentTimeMillis() //添加的时间
) {
    //1完成,0未完成
    val isFinish
        get() = status == 1
}
