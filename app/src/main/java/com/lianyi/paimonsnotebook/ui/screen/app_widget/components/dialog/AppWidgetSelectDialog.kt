package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.dialog

import androidx.compose.runtime.Composable
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetRemoViewsHelper

@Composable
fun AppWidgetSelectDialog(
    appWidgetClsName: String,
    onDismissRequest: () -> Unit
) {
//    val list = AppWidgetRemoViewsHelper.getSupportRemoteViewsListByAppWidgetClsName(appWidgetClsName)


    LazyColumnDialog(title = "小组件选择", onDismissRequest = onDismissRequest) {


    }
}