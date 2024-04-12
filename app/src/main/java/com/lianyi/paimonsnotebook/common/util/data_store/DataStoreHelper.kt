package com.lianyi.paimonsnotebook.common.util.data_store

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.util.json.JSON

/*
* 本地持久化存储管理器
*
* */
val Context.datastorePf by preferencesDataStore(name = "datastorePf")

object DataStoreHelper {

    //操作本地存储的map
    suspend fun <K, V> applyLocalDataMap(
        key: Preferences.Key<String>,
        block: MutableMap<K, V>.() -> Unit
    ) {
        val map = getLocalDataMap<K, V>(key).apply(block)

        //更新本地数据
        key.editValue(JSON.stringify(map))
    }

    suspend fun <K, V> getLocalDataMap(
        key: Preferences.Key<String>
    ): MutableMap<K, V> {
        return dataStoreValuesFirstLambda {
            val json = this[key] ?: JSON.EMPTY_OBJ

            JSON.parse<Map<K, V>>(
                json,
            ).toMutableMap()
        }
    }
}