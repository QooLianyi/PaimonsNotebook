package com.lianyi.paimonsnotebook.ui.screen.items.util

import android.content.Intent
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValuesFirstLambda
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData

object ItemHelper {

    //传递的item_id
    const val PARAM_INT_ITEM_ID = "item_id"


    private fun <T> getDataById(id: Int, items: List<T>, idExpression: (T) -> Int): T? {
        if (id == -1) return null
        return items.takeFirstIf { idExpression.invoke(it) == id }
    }

    suspend fun <T> getItemFromIntent(intent: Intent, items: List<T>, idExpression: (T) -> Int): T {
        val itemId = intent.getIntExtra(PARAM_INT_ITEM_ID, -1)

        val lastViewId = dataStoreValuesFirstLambda {
            when (items.first()) {
                is WeaponData -> {
                    this[PreferenceKeys.LastViewWeaponId] ?: -1
                }

                is AvatarData -> {
                    this[PreferenceKeys.LastViewAvatarId] ?: -1
                }

                else -> {
                    -1
                }
            }
        }

        if (itemId != -1) {
            val item = getDataById(itemId, items, idExpression)
            if (item != null) return item
        }

        return getDataById(lastViewId, items, idExpression) ?: items.last()
    }
}