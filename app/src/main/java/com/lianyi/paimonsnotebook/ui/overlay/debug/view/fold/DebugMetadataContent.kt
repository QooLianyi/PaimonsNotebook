package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DebugMetadataContent() {

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            MetadataHelper.updateMetadata({},{},{})
        }
    }) {
        Text(text = "同步元数据", fontSize = 16.sp)
    }

}