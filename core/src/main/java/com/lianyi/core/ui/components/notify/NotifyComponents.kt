package com.lianyi.core.ui.components.notify

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lianyi.core.common.notify.NotifyHelper
import com.lianyi.core.ui.components.spacer.StatusBarPaddingSpacer

/*
* 通知组件
* */
@Composable
fun NotifyGroup() {
    Box(
        modifier = Modifier
            .zIndex(100f)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp, 12.dp, 12.dp, 0.dp),
            horizontalAlignment = Alignment.End
        ) {
            StatusBarPaddingSpacer()

            NotifyHelper.notifications.forEachIndexed { index, data ->
                key(data.notificationId) {
                    if (index != 0) {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                    NotifyCard(data = data)
                }
            }
        }
    }
}
