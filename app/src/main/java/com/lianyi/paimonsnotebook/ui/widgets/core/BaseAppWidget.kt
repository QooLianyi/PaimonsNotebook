package com.lianyi.paimonsnotebook.ui.widgets.core

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.extension.scope.launchMain
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.common.view.HoyolabWebActivity
import com.lianyi.paimonsnotebook.ui.screen.app_widget.view.AppWidgetConfigurationScreen
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.widgets.remoteviews.state.NoBindingRemoteViews
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetRemoViewsHelper
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
            updateAppWidget(it, null)
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

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        updateAppWidget(appWidgetId, null)
    }

    override fun onReceive(context: Context, intent: Intent?) {
        val appWidgetId = intent?.getIntExtra(AppWidgetHelper.PARAM_APPWIDGET_ID, -1) ?: -1

        when (intent?.action) {
            //由用户调用的刷新
            AppWidgetHelper.ACTION_UPDATE_WIDGET -> {
                if (appWidgetId != -1) {
                    "内容更新中".show()

                    updateAppWidget(appWidgetId, intent)
                }
            }

            //前往配置界面
            AppWidgetHelper.ACTION_GO_CONFIGURATION -> {
                goAppWidgetConfigurationScreen(context, appWidgetId)
            }

            //前往验证界面
            AppWidgetHelper.ACTION_GO_VALIDATE->{
                val mid = intent.getStringExtra("mid") ?: ""
                goValidateScreen(context,mid)
            }
        }

        super.onReceive(context, intent)
    }

    /*
    * 更新组件
    *
    * appWidgetId:组件id
    * intent:意图
    * onlyUpdateStyle:是否只更新样式
    * */
    private fun updateAppWidget(
        appWidgetId: Int,
        intent: Intent?,
        notify:Boolean = false,
    ) {
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
                AppWidgetRemoViewsHelper.getRemoteViews(
                    appWidgetBinding,
                    user,
                    intent
                )
            }

            updateAppWidget(appWidgetId, views)
            if(notify){
                launchMain {
                    "更新完成".show()
                }
            }
        }
    }

    //携带id与当前组件类名前往桌面组件配置界面
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

    private fun goValidateScreen(context: Context,mid:String){
        HomeHelper.goActivityByIntent {
            component = ComponentName(context,HoyolabWebActivity::class.java)
            putExtra("mid",mid)
        }
    }

    //更新组件
    private fun updateAppWidget(appWidgetId: Int, views: RemoteViews) {
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
    }
}