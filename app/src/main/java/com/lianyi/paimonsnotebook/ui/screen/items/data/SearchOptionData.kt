package com.lianyi.paimonsnotebook.ui.screen.items.data

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.util.enums.SortOrderBy
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType

/*
* 角色排序和排序依据
*
* sortBy:排序依据
* iconUrl:图标url
* iconResId:图标res资源id
* name:名称
* value:携带的值
* initOrderBy:初始的排序方向
* contentSlot:内容插槽
* */
data class SearchOptionData(
    val sortBy: ItemFilterType = ItemFilterType.Default,
    val iconUrl: String = "",
    val iconResId: Int = -1,
    val name: String = "",
    val value:Int = -1,
    val initOrderBy: SortOrderBy = SortOrderBy.None,
    val contentSlot: (@Composable () -> Unit)? = null
) {
    //排序方向
    var orderBy by mutableStateOf(SortOrderBy.None)
    init {
        orderBy = initOrderBy
    }
}