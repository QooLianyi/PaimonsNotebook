package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirstLambda
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

    var input by remember {
        mutableStateOf("")
    }

    Column {
        Text(text = content)

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                val res = PublicDataApiClient().getFp(
                    dataStoreValuesFirstLambda {
                        this[PreferenceKeys.DeviceFp] ?: ""
                    }
                )
                content = JSON.stringify(res)
            }
        }) {
            Text(text = "获取device_fp")
        }
    }

    Column {
        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                PreferenceKeys.DeviceId.editValue("")
            }
        }) {
            Text(text = "删除DeviceId")
        }
    }

    Column {

        InputTextFiled(value = input, onValueChange = {
            input = it
        })

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                PreferenceKeys.DeviceId.editValue(input)
            }
        }) {
            Text(text = "设置DeviceId")
        }
    }
}

