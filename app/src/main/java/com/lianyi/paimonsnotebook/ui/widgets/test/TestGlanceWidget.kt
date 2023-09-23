package com.lianyi.paimonsnotebook.ui.widgets.test

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text

class TestGlanceWidget : GlanceAppWidget() {

    companion object {
        val countKey = intPreferencesKey("testGlanceWidgetCount")
    }

    @Composable
    override fun Content() {
        val count = currentState(key = countKey) ?: 0
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
            horizontalAlignment = Alignment.Horizontal.CenterHorizontally
        ) {
            Text(text = count.toString())
            Button(
                text = "增加",
                onClick = actionRunCallback(TestGlanceWidgetActionCallback::class.java)
            )
        }
    }
}

class TestGlanceWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = TestGlanceWidget()
}

class TestGlanceWidgetActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters,
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            val count = prefs[TestGlanceWidget.countKey]
            if (count != null) {
                prefs[TestGlanceWidget.countKey] = count + 1
            } else {
                prefs[TestGlanceWidget.countKey] = 1
            }
        }
        TestGlanceWidget().update(context, glanceId)
    }
}