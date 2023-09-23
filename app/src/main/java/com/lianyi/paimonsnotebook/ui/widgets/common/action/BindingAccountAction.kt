package com.lianyi.paimonsnotebook.ui.widgets.common.action

import android.content.Context
import android.content.Intent
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import com.lianyi.paimonsnotebook.ui.screen.app_widget.view.AppWidgetBindingScreen

class BindingAccountAction : ActionCallback {

    companion object {
        val appWidgetNameKey by lazy {
            ActionParameters.Key<String>("appWidgetName")
        }
    }

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {

        println("action")

        val appWidgetId = GlanceAppWidgetManager(context).getAppWidgetId(glanceId)

        val appWidgetName = parameters[appWidgetNameKey] ?: ""

        context.startActivity(Intent(context, AppWidgetBindingScreen::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putExtra("appWidgetId", appWidgetId)
            putExtra("appWidgetName", appWidgetName)
        })

    }
}