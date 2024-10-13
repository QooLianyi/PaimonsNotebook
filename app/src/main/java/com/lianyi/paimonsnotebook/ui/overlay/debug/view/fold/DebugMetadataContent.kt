package com.lianyi.paimonsnotebook.ui.overlay.debug.view.fold

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.datastorePf
import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun DebugMetadataContent() {

    Text(text = "EnableMetadata")

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            PreferenceKeys.EnableMetadata.editValue(false)
        }
    }) {
        Text(text = "重置元数据标识为false", fontSize = 16.sp)
    }

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            PreferenceKeys.EnableMetadata.editValue(true)
        }
    }) {
        Text(text = "重置元数据标识为true", fontSize = 16.sp)
    }

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            FileHelper.saveFileMetadataPath.listFiles()?.forEach {
                it.delete()
            }
        }
    }) {
        Text(text = "删除全部元数据", fontSize = 16.sp)
    }

    Text(text = "InitialMetadataDownload")

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            PreferenceKeys.InitialMetadataDownload.editValue(true)
        }
    }) {
        Text(text = "重置元数据初始标识为true", fontSize = 16.sp)
    }

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            PreferenceKeys.InitialMetadataDownload.editValue(false)
        }
    }) {
        Text(text = "重置元数据初始标识为false", fontSize = 16.sp)
    }

    Text(text = "OnLaunchShowEnableMetadataHint")


    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            PreferenceKeys.OnLaunchShowEnableMetadataHint.editValue(true)
        }
    }) {
        Text(text = "重置元数据提示标识为true", fontSize = 16.sp)
    }

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            PreferenceKeys.OnLaunchShowEnableMetadataHint.editValue(false)
        }
    }) {
        Text(text = "重置元数据提示标识为false", fontSize = 16.sp)
    }

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            PaimonsNotebookApplication.context.datastorePf.edit {
                it.remove(PreferenceKeys.OnLaunchShowEnableMetadataHint)
                it.remove(PreferenceKeys.EnableMetadata)
                it.remove(PreferenceKeys.InitialMetadataDownload)
            }
        }
    }) {
        Text(text = "重置全部元数据相关状态", fontSize = 16.sp)
    }

    Button(onClick = {
        CoroutineScope(Dispatchers.IO).launch {
            MetadataHelper.updateMetadata(true,{}, {}, {}, {})
        }
    }) {
        Text(text = "同步元数据", fontSize = 16.sp)
    }

}