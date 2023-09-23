package com.lianyi.paimonsnotebook.common.extension.data_store

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.lianyi.paimonsnotebook.common.application.PaimonsNotebookApplication
import com.lianyi.paimonsnotebook.common.util.data_store.datastorePf

suspend fun <T> Preferences.Key<T>.editValue(value: T) =
    PaimonsNotebookApplication.context.datastorePf.edit { it[this] = value }