package com.lianyi.paimonsnotebook.ui.screen.app_widget.viewmodel

import android.content.ComponentName
import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.data.hoyolab.user.User
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.data.AppWidgetConfiguration
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.database.user.util.AccountHelper
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.ui.screen.account.view.AccountManagerScreen
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppWidgetBindingScreenViewModel : ViewModel() {
    private val dao by lazy {
        PaimonsNotebookDatabase.database.appWidgetBindingDao
    }

    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    var appwidgetId: Int? = null
    var appwidgetClassName: String? = null

    var hasUser by mutableStateOf(false)

    init {
        CoroutineScope(Dispatchers.Unconfined).launch {
            AccountHelper.hasUserFlow.collect{
                hasUser = it
            }
        }
    }

    fun goAccountManagerScreen(){
        HomeHelper.goActivity(AccountManagerScreen::class.java)
    }

    fun bindAppwidget(user: User, gameRoleData: UserGameRoleData.Role) {
        if (appwidgetId == null || appwidgetClassName == null) return

//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                val appWidgetBinding = AppWidgetBinding(
//                    appWidgetId = appwidgetId!!,
//                    userEntityMid = user.userEntity.mid,
//                    gameUid = gameRoleData.game_uid,
//                    appwidgetClassName = appwidgetClassName!!,
//                    region = gameRoleData.region,
//                    configuration = AppWidgetConfiguration()
//                )
//                dao.insert(appWidgetBinding)
//
//                val intent = Intent(AppWidgetHelper.ACTION_UPDATE_WIDGET).apply {
//                    component = ComponentName(context, appWidgetBinding.appwidgetClassName)
//                    data = AppWidgetHelper.getUnionData(appwidgetId!!, appwidgetClassName!!)
//                    putExtra(AppWidgetHelper.PARAM_APPWIDGET_ID, appwidgetId)
//                }
//                context.sendBroadcast(intent)
//            } catch (e: Exception) {
//                "添加组件时发生错误:${e.message}".show()
//            }
//        }

    }
}