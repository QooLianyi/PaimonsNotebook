package com.lianyi.paimonsnotebook.common.database.cultivate.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItemMaterials

@Dao
interface CultivateItemMaterialsDao {

    @Upsert
    fun insert(material: CultivateItemMaterials)

    @Upsert
    fun insert(materials: List<CultivateItemMaterials>)

    @Update
    fun update(material: CultivateItemMaterials)

    @Query("delete from cultivate_item_materials")
    fun deleteAll(): Int

    //更新材料状态
    @Query("update cultivate_item_materials set status = :status where project_id = :projectId and cultivate_item_id = :cultivateItemId and item_id = :materialId")
    fun updateStatusByMaterialId(
        status: Int,
        projectId: Int,
        cultivateItemId: Int,
        materialId: Int
    ): Int

    @Query("update cultivate_item_materials set status = :status where project_id = :projectId and cultivate_item_id = :cultivateItemId and item_id in (:materialIds)")
    fun updateStatusByMaterialIds(
        status: Int,
        projectId: Int,
        cultivateItemId: Int,
        materialIds: List<Int>
    ): Int


    @Query("select * from cultivate_item_materials where project_id = :projectId and cultivate_item_id = :cultivateItemId and item_id = :materialId")
    fun getMaterialByCultivateItemId(
        projectId: Int,
        cultivateItemId: Int,
        materialId: Int
    ): CultivateItemMaterials?
}