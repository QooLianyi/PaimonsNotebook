package com.lianyi.paimonsnotebook.ui.widgets.core

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.ui.screen.app_widget.view.AppWidgetConfigurationScreen
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.EmptyRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.NoBindingRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetRemoViewsHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsIndexes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
* 小组件基类
* */
open class BaseAppWidget : AppWidgetProvider() {
    private val dao by lazy {
        PaimonsNotebookDatabase.database.appWidgetBindingDao
    }
    private val context by lazy {
        PaimonsNotebookApplication.context
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?,
    ) {
        appWidgetIds?.forEach {
            validateAndUpdateWidget(it, null)
        }
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        CoroutineScope(Dispatchers.IO).launch {
            appWidgetIds?.forEach {
                dao.deleteByAppWidgetId(it)
            }
        }
        super.onDeleted(context, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val appWidgetId = intent?.getIntExtra(AppWidgetHelper.PARAM_APPWIDGET_ID, -1) ?: -1

        when (intent?.action) {
            //由用户调用的刷新
            AppWidgetHelper.ACTION_UPDATE_WIDGET -> {
                if (appWidgetId != -1) {
                    validateAndUpdateWidget(appWidgetId, intent)
                }
            }

            AppWidgetHelper.ACTION_GO_CONFIGURATION -> {
                goAppWidgetConfigurationScreen(context, appWidgetId)
            }
        }

        super.onReceive(context, intent)
    }

    //验证并更新组件
    private fun validateAndUpdateWidget(appWidgetId: Int, intent: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val appWidgetBinding = dao.getAppWidgetBindingByAppWidgetId(appWidgetId)

            val user = if (appWidgetBinding?.userEntityMid?.isEmpty() == false) {
                appWidgetBinding.getUserEntity()
            } else {
                null
            }

            val views = if (appWidgetBinding == null) {
                NoBindingRemoteViews(appWidgetId, this@BaseAppWidget::class.java)
            } else {
                AppWidgetRemoViewsHelper.getRemoteViews(appWidgetBinding, user, intent)
            }

            updateAppWidget(appWidgetId, views)
        }
    }

    //携带id与当前组件类名前往小组件配置界面
    private fun goAppWidgetConfigurationScreen(context: Context, appWidgetId: Int) {
        val intent = Intent(context, AppWidgetConfigurationScreen::class.java).apply {
            putExtra(AppWidgetHelper.PARAM_APPWIDGET_ID, appWidgetId)
            putExtra(
                AppWidgetHelper.PARAM_APPWIDGET_CLASS_NAME,
                this@BaseAppWidget::class.java.name
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    private fun updateAppWidget(appWidgetId: Int, views: RemoteViews) {
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
    }
}