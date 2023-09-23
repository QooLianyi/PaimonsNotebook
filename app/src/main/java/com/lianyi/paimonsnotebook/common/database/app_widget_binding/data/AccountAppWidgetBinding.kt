package com.lianyi.paimonsnotebook.common.database.app_widget_binding.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_widget_account_binding")
data class AccountAppWidgetBinding(
    @PrimaryKey
    @ColumnInfo(name = "app_widget_id")
    val appWidgetId:Int,
    @ColumnInfo(name = "account_uid")
    val accountUid:String
)
