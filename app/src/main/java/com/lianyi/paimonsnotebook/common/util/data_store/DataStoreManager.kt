package com.lianyi.paimonsnotebook.common.util.data_store

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

/*
* 本地持久化存储管理器
*
* */
val Context.datastorePf by preferencesDataStore(name = "datastorePf")

object DataStoreManager {

}