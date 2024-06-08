package com.lianyi.paimonsnotebook.common.database.cultivate.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Success
import com.lianyi.paimonsnotebook.ui.theme.Warning

/*
* 养成计划物品所需材料表
*
* 使用计划表与材料表的主键作为复合主键
* 删除时一同执行删除
* */
@Entity(
    "cultivate_item_materials",
    foreignKeys = [
        ForeignKey(
            entity = CultivateItems::class,
            parentColumns = arrayOf("item_id", "project_id"),
            childColumns = arrayOf("cultivate_item_id", "project_id"),
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    /*
    * 使用材料的item_id与计算的实体id与计划id组合成一个唯一的主键
    * */
    primaryKeys = [
        "item_id",
        "cultivate_item_id",
        "project_id"
    ]
)
data class CultivateItemMaterials(
    @ColumnInfo("item_id")
    val itemId: Int, //材料id
    @ColumnInfo("cultivate_item_id")
    val cultivateItemId: Int, //所属计算物品的id
    @ColumnInfo("project_id")
    val projectId: Int, //计划id
    val count: Int, //所需数量
    @ColumnInfo("lack_count")
    val lackCount: Int, //缺少数量
    val status: Int //状态
) {

    /*
    * 用于临时存储完成状态
    * 避免UI与数据库状态不一致
    * */
    @Ignore
    var tempStatus = status
        private set

    //1完成,0未完成
    @Ignore
    var isFinish: Boolean = tempStatus == 1
        private set

    fun switchTempStatus() {
        tempStatus = if (isFinish) 0 else 1
        isFinish = tempStatus == 1
    }

    fun getShowContentAndColor(showLackNum: Boolean) =
        if (showLackNum && isFinish) {
            "完成" to Success
        } else if (showLackNum && lackCount >= 0) {
            "$lackCount" to Warning
        } else if (showLackNum) {
            "$count" to Warning
        } else {
            "$count" to Black
        }
}
