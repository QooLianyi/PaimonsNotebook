package com.lianyi.paimonsnotebook.ui.overlay.debug.view

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.components.text.FoldTextContent
import com.lianyi.paimonsnotebook.common.service.overlay.core.OverlayState
import com.lianyi.paimonsnotebook.common.service.overlay.debug.DebugOverlayService
import com.lianyi.paimonsnotebook.common.service.util.ServiceHelper
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold.DebugAppWidgetContent
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold.DebugDeviceInfoContent
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold.DebugDynamicSecretContent
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold.DebugGachaContent
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold.DebugMetadataContent
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold.DebugPostContent
import com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold.DebugTempContent
import com.lianyi.paimonsnotebook.ui.overlay.debug.viewmodel.DebugOverlayViewModel
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 调试面板内容
* */
@Composable
fun DebugOverlayContent(overlayState: OverlayState) {
    val viewModel = remember {
        DebugOverlayViewModel()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
            .onGloballyPositioned {
                overlayState.overlayWidthPx = it.size.width
                overlayState.overlayHeightPx = it.size.height
            },
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "controller:${overlayState.controllerOffset}")
                Button(onClick = {
                    PaimonsNotebookApplication.context.apply {
                        val file =
                            this.getExternalFilesDir(null)?.resolve("debug")
                        file?.delete()
                        val intent = Intent(this, DebugOverlayService::class.java).apply {
                            putExtra(ServiceHelper.Command, ServiceHelper.Command_Exit)
                        }
                        startService(intent)
                    }
                }) {
                    Text(text = "停用调试面板", fontSize = 20.sp)
                }
            }
        }

        item {
            FoldTextContent(titleSlot = {
                Text(text = "设备信息", fontSize = 20.sp)
            }) {
                DebugDeviceInfoContent()
            }
        }

        item {
            FoldTextContent(titleSlot = {
                Text(text = "DS", fontSize = 20.sp)
            }) {
                DebugDynamicSecretContent(
                    dsSaltTypes = viewModel.dsSaltType,
                    dsVersions = viewModel.dsVersion
                )
            }
        }

        item {
            FoldTextContent(titleSlot = {
                Text(text = "Post", fontSize = 20.sp)
            }) {
                DebugPostContent()
            }
        }

        item {
            FoldTextContent(titleSlot = {
                Text(text = "AppWidget", fontSize = 20.sp)
            }) {
                DebugAppWidgetContent()
            }
        }

        item {
            FoldTextContent(titleSlot = {
                Text(text = "Metadata元数据", fontSize = 20.sp)
            }) {
                DebugMetadataContent()
            }
        }

        item {
            FoldTextContent(titleSlot = {
                Text(text = "祈愿", fontSize = 20.sp)
            }) {
                DebugGachaContent()
            }
        }

        item {
            FoldTextContent(titleSlot = {
                Text(text = "临时测试功能", fontSize = 20.sp)
            }) {
                DebugTempContent()
            }
        }
    }
}