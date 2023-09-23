package com.lianyi.paimonsnotebook.ui.screen.items.util

import android.content.Intent
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf

object ItemHelper {

    //传递的item_id
    const val PARAM_INT_ITEM_ID = "item_id"

    fun <T> getItemFromIntent(intent: Intent, items: List<T>, idExpression: (T) -> Int): T {
        val itemId = intent.getIntExtra(PARAM_INT_ITEM_ID, -1)

        return if (itemId != -1) {
            items.takeFirstIf { idExpression.invoke(it) == itemId } ?: items.last()
        } else {
            items.last()
        }
    }
}