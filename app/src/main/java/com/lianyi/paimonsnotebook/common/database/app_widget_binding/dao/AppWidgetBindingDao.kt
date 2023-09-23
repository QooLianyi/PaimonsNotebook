package com.lianyi.paimonsnotebook.common.database.app_widget_binding.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import kotlinx.coroutines.flow.Flow

@Dao
interface AppWidgetBindingDao {
    @Upsert
    fun insert(data: AppWidgetBinding)

    @Query("select * from appwidget_binding")
    fun getAllAppWidgetBinding():Flow<List<AppWidgetBinding>>

    @Query("select * from appwidget_binding where appwidget_id = :appWidgetId")
    fun getAppWidgetBindingByAppWidgetId(appWidgetId: Int): AppWidgetBinding?

    //通过mid获取绑定的组件
    @Query("select * from appwidget_binding where user_entity_mid = :mid")
    fun getAppWidgetBindingByUserMid(mid: String): List<AppWidgetBinding>

    @Query("delete from appwidget_binding where appwidget_id = :appWidgetId")
    fun deleteByAppWidgetId(appWidgetId: Int)

    @Query("delete from appwidget_binding where user_entity_mid = :mid")
    fun deleteByUserMid(mid:String)
}