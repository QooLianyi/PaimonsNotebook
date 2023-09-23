package com.lianyi.paimonsnotebook.ui.widgets.resin.receiver

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.lianyi.paimonsnotebook.ui.widgets.resin.widget.ResinHorizontalWidget
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResinHorizontalWidgetReceiver : GlanceAppWidgetReceiver() {

    override val glanceAppWidget: GlanceAppWidget = ResinHorizontalWidget(null)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        println("ResinHorizontalWidgetReceiver Update -------------")

        CoroutineScope(Dispatchers.Default).launch {
            appWidgetIds.forEach { id ->
            }
        }
    }

}