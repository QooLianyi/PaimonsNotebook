package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationPaddingSpacer
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.entity.AppWidgetBinding
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.AppWidgetAlreadyData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsPreviewHelper

@Composable
internal fun AlreadyBindingAppWidgetPage(
    list: List<AppWidgetAlreadyData>,
    onClick: (AppWidgetBinding) -> Unit
) {
    val groupBy = remember {
        list.groupBy { it.aid.ifEmpty { "默认组件" } }.toList()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(6.dp, 12.dp)
    ) {
        items(groupBy) { pair ->

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(text = pair.first, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                pair.second.forEach { data ->
                    val preview =
                        RemoteViewsPreviewHelper.getPreviewByRemoteViewsClassName(data.appWidgetBinding.remoteViewsClassName)
                    Box(modifier = Modifier
                        .radius(8.dp)
                        .clickable {
                            onClick.invoke(data.appWidgetBinding)
                        }
                    ) {
                        preview.invoke(RemoteViewsPreviewAnimData())
                    }
                }
            }
        }

        item {
            NavigationPaddingSpacer()
        }
    }

}