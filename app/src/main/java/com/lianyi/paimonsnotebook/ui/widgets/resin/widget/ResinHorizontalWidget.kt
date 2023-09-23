package com.lianyi.paimonsnotebook.ui.widgets.resin.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.*
import androidx.glance.action.actionParametersOf
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.cornerRadius
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_10
import com.lianyi.paimonsnotebook.ui.theme.Black_50
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.widgets.common.action.BindingAccountAction
import com.lianyi.paimonsnotebook.ui.widgets.resin.action.GetDailyNoteAction
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetUtil

class ResinHorizontalWidget(private val dailyNoteData: DailyNoteData?) : GlanceAppWidget() {

    companion object {
        val bindingAction by lazy {
            actionRunCallback<BindingAccountAction>(
                parameters = actionParametersOf(
                    BindingAccountAction.appWidgetNameKey to ResinHorizontalWidget::class.java.name
                )
            )
        }
    }

    override val stateDefinition: GlanceStateDefinition<*>
        get() = PreferencesGlanceStateDefinition

    override suspend fun onDelete(context: Context, glanceId: GlanceId) {
        super.onDelete(context, glanceId)

        //当组件移除时,同时移除本地的appWidgetLayout-$appWidgetId文件
        AppWidgetUtil.deleteAppWidgetLayoutFile(glanceId)
    }

    @Composable
    override fun Content() {
        Box(contentAlignment = Alignment.Center, modifier = GlanceModifier.wrapContentSize()) {
            when (currentState(key = AppWidgetUtil.currentState)) {
                "empty", "", null -> {
                    EmptyWidget()
                }
                "success" -> {
                    SuccessResinWidget(dailyNoteData = dailyNoteData)
                }
                else -> {
                    LoadingWidget()
                }
            }
        }
    }
}

@Composable
private fun SuccessResinWidget(dailyNoteData: DailyNoteData?) {
    Row(
        modifier = GlanceModifier.cornerRadius(10.dp).padding(10.dp)
            .background(ColorProvider(White))
            .clickable(onClick = actionRunCallback<GetDailyNoteAction>()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            provider = ImageProvider(R.drawable.icon_resin), contentDescription = "",
            modifier = GlanceModifier.size(40.dp)
        )

        Spacer(modifier = GlanceModifier.width(6.dp))

        Text(
            text = "${dailyNoteData?.current_resin ?: 0}/${dailyNoteData?.max_resin ?: 0}",
            style = TextStyle(
                color = ColorProvider(Black),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }

}

@Composable
fun EmptyWidget() {

    Row(
        modifier = GlanceModifier
//            .padding(10.dp).background(ImageProvider(R.drawable.app_widget_background_day))
            .clickable(
                onClick = ResinHorizontalWidget.bindingAction
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

//        Image(
//            provider = ImageProvider(R.drawable.icon_emo_idle), contentDescription = "",
//            modifier = GlanceModifier.size(45.dp).clickable(
//                onClick = ResinHorizontalWidget.bindingAction
//            )
//        )

        Spacer(modifier = GlanceModifier.width(6.dp))

        Text(
            text = "当前组件未绑定",
            style = TextStyle(
                color = ColorProvider(Black),
                fontSize = 16.sp
            ),
            modifier = GlanceModifier.clickable(
                onClick = ResinHorizontalWidget.bindingAction
            )
        )
    }

//    Box(modifier = GlanceModifier.background(ColorProvider(Black_50))) {
//
//    }

}

@Composable
fun LoadingWidget() {
    Row(
        modifier = GlanceModifier.cornerRadius(10.dp).padding(10.dp)
            .background(ColorProvider(White)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            provider = ImageProvider(R.drawable.icon_klee_square), contentDescription = "",
            modifier = GlanceModifier.size(65.dp)
        )

        Spacer(modifier = GlanceModifier.width(10.dp))

        Text(
            text = "Loading...",
            style = TextStyle(
                color = ColorProvider(Black),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
