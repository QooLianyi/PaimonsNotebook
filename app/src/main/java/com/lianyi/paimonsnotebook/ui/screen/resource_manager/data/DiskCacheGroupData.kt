package com.lianyi.paimonsnotebook.ui.screen.resource_manager.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import java.io.File

data class DiskCacheGroupData(
    val date: String,
) {
    var isAllSelect by mutableStateOf(false)
    val list = mutableStateListOf<GroupItem>()

    data class GroupItem(
        val data: DiskCache,
        val file: File?,
    ) {
        var select by mutableStateOf(false)
    }
}
