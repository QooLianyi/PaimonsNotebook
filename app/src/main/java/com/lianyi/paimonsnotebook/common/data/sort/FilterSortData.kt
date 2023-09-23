package com.lianyi.paimonsnotebook.common.data.sort

import androidx.compose.runtime.State
import com.lianyi.paimonsnotebook.common.util.enums.SortOrderBy

/*
* 用于筛选排序
*
* */
data class FilterSortData(
    val name:String,
    val value:Float,
    var order:State<SortOrderBy>
)