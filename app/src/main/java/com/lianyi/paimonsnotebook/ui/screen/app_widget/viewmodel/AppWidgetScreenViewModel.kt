package com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.AppWidgetAlreadyData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.view.AppWidgetConfigurationScreen
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsIndexes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppWidgetScreenViewModel : ViewModel() {
    private val appWidgetBindingDao = PaimonsNotebookDatabase.database.appWidgetBindingDao
    private val userDao = PaimonsNotebookDatabase.database.userDao

    val appWidgetBindingList = mutableStateListOf<AppWidgetAlreadyData>()

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            appWidgetBindingDao.getAllAppWidgetBinding().collect { list ->
                val userMap = userDao.getAllUser().associateBy { user ->
                    user.mid
                }

                appWidgetBindingList.clear()

                appWidgetBindingList += list.map { appWidgetBinding ->
                    val uid = userMap[appWidgetBinding.userEntityMid]?.aid ?: ""

                    AppWidgetAlreadyData(
                        aid = uid,
                        appWidgetBinding = appWidgetBinding
                    )
                }
            }
        }
    }

    val tabs = arrayOf("小组件", "我的组件")

    var currentTabIndex by mutableIntStateOf(0)

    fun changeTab(value: Int) {
        currentTabIndex = value
    }

    fun goAppWidgetConfigurationScreen(appWidgetBinding: AppWidgetBinding) {
        HomeHelper.goActivity(AppWidgetConfigurationScreen::class.java, bundle = Bundle().apply {
            putString(
                AppWidgetHelper.PARAM_REMOTE_VIEWS_CLASS_NAME,
                appWidgetBinding.remoteViewsClassName
            )
            putInt(AppWidgetHelper.PARAM_APPWIDGET_ID,appWidgetBinding.appWidgetId)
            putString(
                AppWidgetHelper.PARAM_APPWIDGET_CLASS_NAME,
                RemoteViewsIndexes.getAppWidgetClassNameByRemoteViewsClassName(appWidgetBinding.remoteViewsClassName)
            )
        })
    }

}