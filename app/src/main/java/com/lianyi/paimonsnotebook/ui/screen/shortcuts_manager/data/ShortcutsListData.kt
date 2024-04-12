package com.lianyi.paimonsnotebook.ui.screen.shortcuts_manager.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData

data class ShortcutsListData(
    val modalItemData: ModalItemData
) {
    var selected by mutableStateOf(false)
    //排序参照
    var index = 99

    //重置内部值
    fun reset(){
        selected = false
        index = 99
    }

}
