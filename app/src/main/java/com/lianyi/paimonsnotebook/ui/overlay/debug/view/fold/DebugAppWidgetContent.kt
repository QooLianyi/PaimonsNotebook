package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import android.content.ComponentName
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.extension.string.warnNotify
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsIndexes
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsPreviewHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun DebugAppWidgetContent() {
    var input by remember {
        mutableStateOf("")
    }

    InputTextFiled(value = input, onValueChange = {
        input = it
    })

    var showAppWidgetList by remember {
        mutableStateOf(false)
    }

    val appWidgetList = remember {
        mutableStateListOf<AppWidgetBinding>()
    }

    LaunchedEffect(showAppWidgetList) {
        if (showAppWidgetList) {
            withContext(Dispatchers.IO) {
                PaimonsNotebookDatabase.database.appWidgetBindingDao.getAllAppWidgetBinding()
                    .collect {
                        appWidgetList.clear()
                        appWidgetList += it
                    }
            }
        }
    }

    Column {

        //删除组件只能由用户调用
//        Button(onClick = {
//            val id = input.toIntOrNull() ?: -1
//
//            if (id == -1) return@Button
//
//            CoroutineScope(Dispatchers.IO).launch {
//                val appwidgetBindingData =
//                    PaimonsNotebookDatabase.database.appWidgetBindingDao.getAppWidgetBindingByAppWidgetId(
//                        id
//                    )
//
//                if (appwidgetBindingData == null) {
//                    "没有id为[${id}]的桌面组件".warnNotify()
//                    return@launch
//                }
//
//                val context = PaimonsNotebookApplication.context
//
//                val remoteViewsInfo =
//                    RemoteViewsIndexes.getRemoteViewsInfoByRemoteViewsClassName(appwidgetBindingData.remoteViewsClassName)
//
//                if (remoteViewsInfo == null) {
//                    "id为[${id}]的桌面组件没有找到对应的远端视图信息".warnNotify()
//                    return@launch
//                }
//
//                val intent = Intent(AppWidgetHelper.ACTION_DELETE_WIDGET).apply {
//                    component = ComponentName(context, remoteViewsInfo.appWidgetClass)
//                    putExtra(AppWidgetHelper.PARAM_APPWIDGET_ID,appwidgetBindingData.appWidgetId)
//                }
//
//                context.sendBroadcast(intent)
//            }
//        }) {
//            Text(text = "删除指定id的桌面组件", fontSize = 16.sp)
//        }


        Button(onClick = {
            val id = input.toIntOrNull() ?: -1

            if (id == -1) return@Button

            CoroutineScope(Dispatchers.IO).launch {
                AppWidgetHelper.updateAppWidgetContentById(id)
            }
        }) {
            Text(text = "更新指定id的桌面组件", fontSize = 16.sp)
        }
        Button(onClick = {
            showAppWidgetList = !showAppWidgetList
        }) {
            Text(text = if (showAppWidgetList) "关闭组件列表" else "开启组件列表", fontSize = 16.sp)
        }

        AnimatedVisibility(visible = showAppWidgetList) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                appWidgetList.forEach { appWidgetBinding ->
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(text = "id = ${appWidgetBinding.appWidgetId}")
                        Text(text = "remoteViews = ${appWidgetBinding.remoteViewsClassName}")
                        Text(text = "mid = ${appWidgetBinding.userEntityMid}")
                        Text(text = "dataType = ${appWidgetBinding.dataType}")
                        Text(text = "configuration = ${appWidgetBinding.configuration}")
                    }
                }
            }
        }
    }
}