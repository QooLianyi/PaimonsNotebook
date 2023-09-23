package com.lianyi.paimonsnotebook.common.util.data_store

import androidx.datastore.preferences.core.Preferences
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import kotlinx.coroutines.flow.map

suspend fun dataStoreValues(
    block:suspend (Preferences)->Unit
){
    PaimonsNotebookApplication.context.datastorePf.data.map {
        it
    }.collect{
        block(it)
    }
}