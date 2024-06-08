package com.lianyi.paimonsnotebook.common.database.cultivate.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateProject
import kotlinx.coroutines.flow.Flow


@Dao
interface CultivateProjectDao {

    @Upsert
    fun insert(project: CultivateProject)

    @Update
    fun update(project: CultivateProject)

    @Query("update cultivate_project set is_selected = 1 where project_id = :projectId")
    fun setSelectedProjectById(projectId: Int)

    @Query("update cultivate_project set is_selected = 0")
    fun setAllProjectUnSelected()

    @Query("update cultivate_project set is_selected = 0 where project_id = :projectId")
    fun setUnSelectedByProjectId(projectId: Int)

    //获取当前选中档案用户流
    @Query("select * from cultivate_project where is_selected = 1")
    fun getSelectedProjectFlow(): Flow<CultivateProject?>

    @Query("select * from cultivate_project")
    fun getCultivateProjectListFlow(): Flow<List<CultivateProject>>

    @Query("select count(project_id) from cultivate_project where project_name = :name")
    fun hasSameNameCultivateProject(name: String): Boolean

    //获取当前选中档案用户
    @Query("select * from cultivate_project where is_selected = 1")
    fun getSelectedProject(): CultivateProject?

    @Query("delete from cultivate_project where project_id = :projectId")
    fun deleteProjectById(projectId: Int)

    @Query("delete from cultivate_project")
    fun deleteAll(): Int

    @Transaction
    suspend fun updateSelectedProject(unSelectProjectId: Int, selectProjectId: Int) {
        setUnSelectedByProjectId(unSelectProjectId)
        setSelectedProjectById(selectProjectId)
    }
}