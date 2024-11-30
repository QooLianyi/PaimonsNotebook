package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.popup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.layout.FoldTextContent
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.widget.TextButton
import com.lianyi.paimonsnotebook.common.database.app_widget_binding.util.AppWidgetDataType
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.edit.binding.AppWidgetBindingService

@Composable
fun BoxScope.AppWidgetFieldBindingPanel(
    offsetX: Dp,
    alignment: Alignment,
    service: AppWidgetBindingService,
    onClickField: (AppWidgetDataType, String) -> Unit
) {
    ContentSpacerLazyColumn(
        modifier = Modifier
            .align(alignment)
            .offset(x = offsetX)
            .width(180.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        service.bindingClassMap.forEach { (appWidgetDataType, classMap) ->
            item {
                FoldTextContent(title = appWidgetDataType.name) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        classMap.forEach { fieldName ->
                            Text(
                                text = fieldName,
                                fontSize = 14.sp,
                                modifier = Modifier
                                    .radius(2.dp)
                                    .clickable {
                                        onClickField.invoke(appWidgetDataType, fieldName)
                                    }
                                    .padding(2.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            TextButton(text = "移除绑定") {
                onClickField.invoke(AppWidgetDataType.None, "#")
            }
        }
    }
}