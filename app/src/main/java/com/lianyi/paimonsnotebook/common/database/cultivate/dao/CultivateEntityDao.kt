package com.lianyi.paimonsnotebook.common.database.cultivate.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateEntity
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItems
import kotlinx.coroutines.flow.Flow

@Dao
interface CultivateEntityDao {

    @Upsert
    fun insert(entity: CultivateEntity)

    @Upsert
    fun insert(list: List<CultivateEntity>)

    @Update
    fun update(entity: CultivateEntity)

    @Delete
    fun delete(entity: CultivateEntity)

    @Delete
    fun delete(list: List<CultivateEntity>)

    /*
    * 获取当前计划所有实体与对应养成item列表
    * key = 养成实体 value = 养成实体项列表
    * */
    @Query(
        """
            select * from cultivate_entity 
            join cultivate_items
            on cultivate_entity.project_id = :projectId and cultivate_entity.item_id = cultivate_items.entity_item_id
        """
    )
    fun getCultivateEntityMapListFlowByProjectId(projectId: Int): Flow<Map<CultivateEntity, List<CultivateItems>>>

    /*
    * 删除所有完成的实体
    * 获取总览数据的cultivate_items并在materials表里查找status为0(未完成)个数为0的数据(全部完成的)
    * 返回entity_itemId集合,并判断是否在itemId集合中
    *
    * having sum(1 - cultivate_item_materials.status) = 0;因为完成的材料状态为1,1 - 1 = 0,求和后还是0,可以筛选出所有完成的养成项
    * */
    @Query(
        """
            delete from cultivate_entity
            where cultivate_entity.project_id = :projectId and cultivate_entity.item_id in(
                    select cultivate_entity.item_id from cultivate_entity join cultivate_items
                    on cultivate_entity.project_id = :projectId and cultivate_entity.project_id = cultivate_items.project_id and -cultivate_entity.item_id = cultivate_items.item_id
                    where cultivate_items.item_id in(
                            select cultivate_items.item_id from cultivate_item_materials,cultivate_items
                            on cultivate_item_materials.project_id = :projectId and cultivate_items.project_id = :projectId and cultivate_item_materials.cultivate_item_id = cultivate_items.item_id
                            group by cultivate_item_materials.cultivate_item_id
                            having sum(1 - cultivate_item_materials.status) = 0
                    )
            )
        """
    )
    fun deleteAllSuccessEntityByProjectId(projectId: Int): Int

    /*
    * 删除所有没有养成项的养成实体
    * */
    @Query(
        """
        delete from cultivate_entity
        where cultivate_entity.project_id = :projectId and cultivate_entity.item_id in (
            select cultivate_entity.item_id from cultivate_entity
            left join cultivate_items
            on cultivate_entity.item_id = cultivate_items.entity_item_id and cultivate_entity.project_id= cultivate_items.project_id
            where cultivate_entity.project_id = :projectId and cultivate_items.entity_item_id is null
        )
    """
    )
    fun deleteAllNoItemsEntityByProjectId(projectId: Int): Int

    /*
    * 删除所有没有养成材料,目标等级不为1的养成项
    * */
    @Query(
        """
            delete from cultivate_items
            where cultivate_items.project_id = :projectId and cultivate_items.item_id in (
                select cultivate_items.item_id from cultivate_items
                left join cultivate_item_materials
                on cultivate_items.item_id = cultivate_item_materials.cultivate_item_id and cultivate_items.project_id = cultivate_item_materials.project_id
                where  cultivate_items.from_level != 1 and  cultivate_items.to_level != 1 and cultivate_items.project_id = :projectId and cultivate_item_materials.cultivate_item_id is null
            )
        """
    )
    fun deleteAllNoMaterialItemsByProjectId(projectId: Int): Int

    /*
   * 删除所有没有养成材料的养成实体
   * 在添加时出现错误可能会出现这样的实体,需要在出错时调用此方法删除错误的实体
   *
   * 一起删除的sql子查询套了好几层要吐了,直接分开删了
   * */
    @Transaction
    suspend fun deleteNoMaterialEntityByProjectId(projectId: Int): Int {
        deleteAllNoMaterialItemsByProjectId(projectId = projectId)
        return deleteAllNoItemsEntityByProjectId(projectId = projectId)
    }

    //删除养成计算实体,根据实体id与养成计划id
    @Query("delete from cultivate_entity where item_id = :itemId and project_id = :projectId")
    fun deleteEntityByItemIdAndProjectId(itemId: Int, projectId: Int): Int

    @Query("delete from cultivate_entity where item_id in(:itemIds) and project_id = :projectId")
    fun deleteEntityByItemIdsAndProjectId(itemIds: List<Int>, projectId: Int): Int

    @Query(
        """
        select count(*) from cultivate_entity where item_id = :itemId and project_id = (
            select project_id from cultivate_project where is_selected = 1
        )
        """
    )
    fun entityHasAddedSelectedProject(itemId: Int): Boolean

    @Query("delete from cultivate_entity")
    fun deleteAll(): Int
}