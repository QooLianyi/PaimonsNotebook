package com.lianyi.paimonsnotebook.ui.widgets.resin.action

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.action.ActionCallback
import com.lianyi.paimonsnotebook.common.extension.string.show
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GetDailyNoteAction : ActionCallback {

    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            "正在更新".show()
        }
        AppWidgetUtil.updateHorizontalResinWidgetByGlanceId(context, glanceId,
            onSuccess = {
                "更新完成".show()
            }, onError = {
                "更新失败:${it}".show()
            })
    }
}