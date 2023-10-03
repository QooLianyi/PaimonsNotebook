package com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.data.AppWidgetConfiguration
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.type_converter.AppWidgetConfigurationConverter
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.type_converter.SetRemoteViewsTypeConverter
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsDataType


/*
* 桌面组件绑定实体表
* appwidgetId:桌面组件id
* userEntityMid:绑定的用户实体,空值代表无需用户
* dataType:桌面组件所需的数据类型
* remoteViewsClassName:绑定的远端视图类名
* configuration:桌面组件配置
* */
@Entity(
    "appwidget_binding",
    indices = [
        Index(
            "user_entity_mid",
            name = "index_user_entity_mid"
        )
    ]
)
@TypeConverters(AppWidgetConfigurationConverter::class, SetRemoteViewsTypeConverter::class)
data class AppWidgetBinding(
    @PrimaryKey
    @ColumnInfo("appwidget_id")
    val appWidgetId: Int,
    @ColumnInfo("user_entity_mid")
    val userEntityMid: String,
    @ColumnInfo("data_type")
    val dataType: Set<RemoteViewsDataType>,
    @ColumnInfo("remote_views_class_name")
    val remoteViewsClassName: String,
    val configuration: AppWidgetConfiguration,
) {
    companion object {
        private val userDao by lazy {
            PaimonsNotebookDatabase.database.userDao
        }
    }

    fun getUserEntity() = userDao.getUserByMid(userEntityMid)
}
