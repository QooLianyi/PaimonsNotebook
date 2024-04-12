package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hoyolab.public_data_api.PublicDataApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DebugDeviceFpContent() {
    var content by remember {
        mutableStateOf("无内容")
    }
    Column {
        Text(text = content)

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val res = PublicDataApiClient().getFp()
                content = JSON.stringify(res)
            }
        }) {
            Text(text = "获取device_fp")
        }

    }
}

