package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.page

import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.extension.string.errorNotify
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import com.lianyi.paimonsnotebook.ui.screen.app_widget.view.AppWidgetConfigurationScreen
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.theme.Black_5
import com.lianyi.paimonsnotebook.ui.widgets.util.AppWidgetHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsIndexes
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsPreviewHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsTypeHelper

@Composable
internal fun AppWidgetOverview(
    enableMetadata: Boolean
) {

    val list = remember {
        RemoteViewsIndexes.list
            .filter {
                it.requireMetadata == enableMetadata || enableMetadata
            }.groupBy { remoteViewsInfo ->
                RemoteViewsTypeHelper.getTypeNameByType(remoteViewsInfo.remoteViewsType)
            }.map {
                it.key to it.value.map { remoteViewsInfo ->
                    val clsName = remoteViewsInfo.remoteViewsClass.name
                    clsName to RemoteViewsPreviewHelper.getPreviewByRemoteViewsClassName(clsName)
                }
            }
    }

    val tempPreviewAnim = RemoteViewsPreviewAnimData()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(12.dp, 6.dp)
    ) {
        items(list) { pair ->
            Column(Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(text = pair.first, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 6.dp)
                        .height(.5.dp)
                        .background(Black_5)
                )

                pair.second.forEach { item ->
                    Box(modifier = Modifier
                        .radius(8.dp)
                        .clickable {
                            val remoteViewsInfo =
                                RemoteViewsIndexes.getRemoteViewsInfoByRemoteViewsClassName(item.first)

                            if (remoteViewsInfo == null) {
                                "所选组件不可用".errorNotify()
                                return@clickable
                            }

                            HomeHelper.goActivity(
                                AppWidgetConfigurationScreen::class.java,
                                bundle = Bundle().apply {
                                    putString(
                                        AppWidgetHelper.PARAM_REMOTE_VIEWS_CLASS_NAME,
                                        item.first
                                    )
                                    putString(
                                        AppWidgetHelper.PARAM_APPWIDGET_CLASS_NAME,
                                        remoteViewsInfo.appWidgetClass.name
                                    )
                                    putInt(
                                        AppWidgetHelper.PARAM_ADD_FLAG,
                                        AppWidgetHelper.FLAG_ADD
                                    )
                                })
                        }
                    ) {
                        item.second.invoke(tempPreviewAnim)
                    }
                }
            }
        }

        item {
            NavigationBarPaddingSpacer()
        }
    }
}