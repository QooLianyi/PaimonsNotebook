package com.lianyi.paimonsnotebook.common.util.data_store

import androidx.datastore.preferences.core.Preferences
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

suspend fun dataStoreValuesFirst(
    block:suspend (Preferences)->Unit
){
    PaimonsNotebookApplication.context.datastorePf.data.map {
        it
    }.first{
        block(it)
        true
    }
}