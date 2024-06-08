package com.lianyi.paimonsnotebook.common.database.cultivate.dao

import androidx.room.Dao
import androidx.room.MapColumn
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItemMaterials
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItems
import kotlinx.coroutines.flow.Flow

@Dao
interface CultivateItemsDao {

    @Upsert
    fun insert(item: CultivateItems)

    @Upsert
    fun insert(items: List<CultivateItems>)

    @Update
    fun update(item: CultivateItems)

    /*
    * key = item_id , value = 同item_id的数据列表
    * */
    @Query(
        """
            select cultivate_items.item_id as `cultivateItemsId`,cultivate_item_materials.* from cultivate_items
            join cultivate_item_materials
            on cultivate_items.project_id = :projectId and cultivate_item_materials.project_id = :projectId and cultivate_items.item_id = cultivate_item_materials.cultivate_item_id 
        """
    )
    fun getCultivateIdsMaterialsMapFlowByProjectId(projectId: Int): Flow<Map<@MapColumn(columnName = "cultivateItemsId") Int, List<CultivateItemMaterials>>>

    @Query("delete from cultivate_items")
    fun deleteAll(): Int
}