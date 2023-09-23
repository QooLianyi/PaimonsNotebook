package com.lianyi.paimonsnotebook.ui.widgets.resin.action

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.ui.widgets.AppWidgetActionParameters
import com.lianyi.paimonsnotebook.ui.widgets.resin.widget.ResinHorizontalWidget

class TestFunc01Action : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        val count = parameters[AppWidgetActionParameters.widgetsCount] ?: 0
        updateAppWidgetState(context, glanceId) {
            it[PreferenceKeys.widgetsCount] = count
        }

//        CoroutineScope(Dispatchers.Default).launch {
//            val bitmap = BitmapFactory.decodeResource(context.resources,R.drawable.icon_klee_square)
//            println("bitmap = ${bitmap}")
//        }

        println("action:glanceId = ${glanceId}")

        ResinHorizontalWidget(null).update(context, glanceId)

//        ResinHorizontalWidget(null).update(context, glanceId)
    }
}