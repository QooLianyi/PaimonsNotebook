package com.lianyi.paimonsnotebook.ui.screen.home.data

import android.app.Activity
import com.lianyi.paimonsnotebook.ui.screen.home.util.FunctionPages

data class FunctionItemData(
    val name: String,
    val icon: Int,
    val target: FunctionPages,
    val offline: Boolean = false,
    val shortcut: Boolean = false,
)

data class ModalItemData(
    val name: String,
    val icon: Int,
    val target: Class<out Activity>,
    val offline: Boolean = false
)