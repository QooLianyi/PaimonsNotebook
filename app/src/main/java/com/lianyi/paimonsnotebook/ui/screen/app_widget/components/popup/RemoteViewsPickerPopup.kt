package com.lianyi.paimonsnotebook.ui.screen.app_widget.components.popup

import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPlaceholder
import com.lianyi.paimonsnotebook.common.components.popup.BasePopup
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.app_widget.data.RemoteViewsPreviewAnimData
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black_5
import com.lianyi.paimonsnotebook.ui.widgets.common.data.RemoteViewsInfo
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsIndexes
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsPreviewHelper
import com.lianyi.paimonsnotebook.ui.widgets.util.RemoteViewsTypeHelper

@Composable
internal fun RemoteViewsPickerPopup(
    remoteViewsClassName: String,
    visible: Boolean,
    onRequestDismiss: () -> Unit,
    onSelect: (RemoteViewsInfo) -> Unit
) {
    val remoteViewsInfo =
        RemoteViewsIndexes.getRemoteViewsInfoByRemoteViewsClassName(remoteViewsClassName)

    val list = remember {
        RemoteViewsIndexes.list.filter { it.appWidgetClass.name == remoteViewsInfo?.appWidgetClass?.name }
            .groupBy { remoteViewsInfo ->
                RemoteViewsTypeHelper.getTypeNameByType(remoteViewsInfo.remoteViewsType)
            }.map {
            it.key to it.value
        }
    }

    BasePopup(visible = visible, onRequestDismiss = onRequestDismiss) {
        Crossfade(targetState = list.isEmpty(), label = "") {
            if (it) {
                EmptyPlaceholder("没有找到支持的小组件")
            } else {
                val tempPreviewAnim = RemoteViewsPreviewAnimData()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor)
                ) {
                    ContentSpacerLazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 6.dp)
                    ) {
                        items(list) { pair ->
                            Column(
                                Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = pair.first,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

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
                                            onSelect.invoke(item)
                                            onRequestDismiss.invoke()
                                        }
                                    ) {
                                        RemoteViewsPreviewHelper.getPreviewByRemoteViewsClassName(
                                            item.remoteViewsClass.name
                                        ).invoke(tempPreviewAnim)
                                    }
                                }
                            }
                        }
                    }

                    Icon(
                        painter = painterResource(id = R.drawable.ic_dismiss),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .radius(2.dp)
                            .size(32.dp)
                            .clickable {
                                onRequestDismiss.invoke()
                            }
                    )
                }
            }
        }
    }
}