package com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.filter

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.lianyi.paimonsnotebook.common.util.enums.ListLayoutStyle
import com.lianyi.paimonsnotebook.common.util.enums.SortOrderBy
import com.lianyi.paimonsnotebook.ui.screen.items.data.SearchOptionData
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/*
* 物品筛选ViewModel
*
* 此处负责处理筛选条件的状态与更新
*
* */
class ItemFilterViewModel<T>(
    private val items: List<T>,
    val searchOptionList: List<Pair<String, List<SearchOptionData>>>,
    val getFilteredItemList: (ItemFilterViewModel<T>,List<T>) -> List<T>,
    private val itemSortCompareBy: (T, type: ItemFilterType) -> Long
) {

    //物品列表
    var itemList by mutableStateOf(items)
        private set

    //筛选的选项映射
    private val selectedOptionMap = mutableStateMapOf<ItemFilterType, Set<Int>>()


    init {
        searchOptionList.forEach { pair ->
            val data = pair.second.first()

            //为列表布局与排序设置默认选中项
            when (data.sortBy) {
                ItemFilterType.ListLayout, ItemFilterType.Default -> selectedOptionMap[data.sortBy] =
                    mutableSetOf(data.value)

                else -> {}
            }
        }
    }

    //是否显示过滤条件内容
    var showFilterContent: Boolean by mutableStateOf(false)
        private set

    //是否显示清除过滤条件选项
    var showClearFilter: Boolean by mutableStateOf(false)
        private set

    //是否显示过滤结果内容
    var showResultList: Boolean by mutableStateOf(false)
        private set

    //输入的文本
    var inputNameValue: String by mutableStateOf("")
        private set

    //搜索结果列表样式
    var itemListLayoutStyle: ListLayoutStyle by mutableStateOf(ListLayoutStyle.ListVertical)
        private set

    //是否更新列表标记,防止筛选条件不变的情况下开关列表导致重复筛选排序
    private var updateList = true

    //当前排序类型,通过该值对列表进行排序
    var currentOrderByKeyType = ItemFilterType.Default
        private set

    //列表倒序
    var reverseList = true
        private set

    //列表滚动状态
    val lazyGridState = LazyGridState()
    val lazyListState = LazyListState()

    //开关筛选结果列表
    fun toggleFilterResultList() {
        showResultList = !showResultList

        if (showResultList) {
            showResultList()
        }
    }

    fun showResultList() {
        if (updateList) {
            onShowResultList()

            listScrollToFirstItem()
            updateList = false
        }
    }

    //当显示结果列表时
    private fun onShowResultList() {
        filterItem()
        sortItem()
    }

    //根据过滤方法获取过滤后的列表
    private fun filterItem() {
        itemList = getFilteredItemList.invoke(this,items)
    }

    //根据当前的排序方法对列表进行排序
    private fun sortItem() {
        val list = mutableListOf<T>().apply {
            this += itemList
        }

        list.sortBy {
            itemSortCompareBy.invoke(it, currentOrderByKeyType)
        }

        if (reverseList) {
            list.reverse()
        }

        itemList = list
    }

    //开关筛选内容
    fun toggleFilterContent() {
        showFilterContent = !showFilterContent
    }

    //关闭筛选内容
    fun dismissFilterContent() {
        showFilterContent = false
    }

    //当输入的名称值发生变化
    fun onInputTextNameValueChange(str: String) {
        inputNameValue = str

        filterItem()

        if (showResultList) {
            sortItem()
            resetListState()
        }

        checkShowClearFilter()
    }


    //重置选中项,默认排序和列表布局
    fun resetFilter() {
        selectedOptionMap.keys.forEach { type ->
            when (type) {
                ItemFilterType.ListLayout, ItemFilterType.Default -> {
                }

                else -> selectedOptionMap.remove(type)
            }
        }

        inputNameValue = ""
        updateList = true
        onShowResultList()
        checkShowClearFilter()

        resetListState()
    }

    //重置列表状态
    private fun resetListState() {
        if (!showResultList) return

        listScrollToFirstItem()
    }
    private fun listScrollToFirstItem(){
        CoroutineScope(Dispatchers.Main).launch {
            lazyListState.scrollToItem(0)
            lazyGridState.scrollToItem(0)
        }
    }

    //如果已选中的选项keys不等于2并且输入的名称不为空则显示清除筛选按钮
    private fun checkShowClearFilter() {
        showClearFilter = selectedOptionMap.keys.size != 2 || inputNameValue.isNotEmpty()
    }

    //当选择选项时,根据不同的选项类型进行不同的处理
    fun onSelectOption(data: SearchOptionData) {
        when (data.sortBy) {
            //设置排序类型,正序倒序
            ItemFilterType.Default -> {

                //判断是否点击了相同的选项,是:反向列表
                data.orderBy =
                    if (selectedOptionMap[data.sortBy]?.contains(data.value) == true && data.orderBy == SortOrderBy.Descend) {
                        SortOrderBy.Ascend
                    } else {
                        SortOrderBy.Descend
                    }


                selectedOptionMap[data.sortBy] = setOf(data.value)

                //判断是否是倒序
                reverseList = data.orderBy == SortOrderBy.Descend

                //从枚举类中找到值为data.value的枚举类型并赋值
                currentOrderByKeyType = ItemFilterType.entries.first { it.ordinal == data.value }
            }

            //设置列表显示格式
            ItemFilterType.ListLayout -> {
                itemListLayoutStyle = ListLayoutStyle.entries.toTypedArray()[data.value]

                selectedOptionMap[data.sortBy] = setOf(itemListLayoutStyle.ordinal)
            }

            //其余的都按照筛选条件来处理
            else -> {
                val set = selectedOptionMap[data.sortBy]
                selectedOptionMap[data.sortBy] = if (set?.contains(data.value) == true) {
                    set.toMutableSet().apply {
                        remove(data.value)
                    }
                } else {
                    mutableSetOf<Int>().apply {
                        this += selectedOptionMap[data.sortBy] ?: setOf()
                        this += data.value
                    }
                }

                if (selectedOptionMap[data.sortBy]?.isEmpty() == true) {
                    selectedOptionMap.remove(data.sortBy)
                }
            }
        }

        //当进行选项时,标记列表更新为true
        updateList = true
        checkShowClearFilter()
    }

    //获取选项的选中状态
    fun getOptionSelectState(data: SearchOptionData): Boolean =
        selectedOptionMap[data.sortBy]?.contains(data.value) == true

    /*
    * 判断某个值是否包含,适用于筛选
    *
    * type:key
    * value:对应key的value
    *
    * false表示不包含
    * true表示包含或type对应的map为空
    * */
    fun filterValueExists(type: ItemFilterType, value: Int): Boolean {
        val set = selectedOptionMap[type]

        return set?.contains(value) ?: true
    }
}