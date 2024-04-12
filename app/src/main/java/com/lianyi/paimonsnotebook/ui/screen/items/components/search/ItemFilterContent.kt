package com.lianyi.paimonsnotebook.ui.screen.items.components.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyVerticalGrid
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.util.enums.ListLayoutStyle
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.BlurButton
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.filter.ItemFilterViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.BlurCardBackgroundColor

@Composable
internal fun <T> ItemFilterContent(
    itemFilterViewModel: ItemFilterViewModel<T>,
    itemSlot: @Composable (data: T, layoutStyle: ListLayoutStyle, type: ItemFilterType) -> Unit
) {
    AnimatedVisibility(
        visible = itemFilterViewModel.showFilterContent,
        enter = slideIn(spring()) {
            IntOffset(x = -it.width, y = 0)
        },
        exit = slideOut(spring()) {
            IntOffset(x = -it.width, y = 0)
        },
        modifier = Modifier.zIndex(5f),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackGroundColor)
        ) {
            StatusBarPaddingSpacer()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                BlurButton(resId = R.drawable.ic_filter) {
                    itemFilterViewModel.toggleFilterResultList()
                }

                InputTextFiled(
                    value = itemFilterViewModel.inputNameValue,
                    onValueChange = itemFilterViewModel::onInputTextNameValueChange,
                    backgroundColor = BlurCardBackgroundColor,
                    borderRadius = 2.dp,
                    modifier = Modifier
                        .height(36.dp)
                        .weight(1f),
                    contentAlignment = Alignment.CenterStart,
                    placeholder = "按名称筛选"
                )

                if (itemFilterViewModel.showClearFilter) {
                    BlurButton(resId = R.drawable.ic_dismiss_circle_full) {
                        itemFilterViewModel.resetFilter()
                    }
                }

                BlurButton(resId = R.drawable.ic_dismiss) {
                    itemFilterViewModel.dismissFilterContent()
                }
            }

            Box {
                ContentSpacerLazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    statusBarPaddingEnabled = false
                ) {
                    items(itemFilterViewModel.searchOptionList) { pair ->
                        SearchOptionGroup(
                            name = pair.first,
                            options = pair.second,
                            getOptionSelectState = itemFilterViewModel::getOptionSelectState,
                            onSelectedItem = itemFilterViewModel::onSelectOption
                        )
                    }
                }

                Column {
                    AnimatedVisibility(
                        visible = itemFilterViewModel.showResultList,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it }
                    ) {
                        when (itemFilterViewModel.itemListLayoutStyle) {
                            ListLayoutStyle.ListVertical -> {
                                ContentSpacerLazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(BackGroundColor),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(8.dp),
                                    state = itemFilterViewModel.lazyListState,
                                    statusBarPaddingEnabled = false
                                ) {

                                    items(itemFilterViewModel.itemList,
                                        key = { System.identityHashCode(it) }
                                    ) {
                                        itemSlot.invoke(
                                            it,
                                            itemFilterViewModel.itemListLayoutStyle,
                                            itemFilterViewModel.orderByType
                                        )
                                    }
                                }
                            }

                            ListLayoutStyle.GridVertical -> {
                                ContentSpacerLazyVerticalGrid(
                                    columns = GridCells.Adaptive(60.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(8.dp),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(BackGroundColor),
                                    state = itemFilterViewModel.lazyGridState,
                                    statusBarPaddingEnabled = false
                                ) {
                                    items(
                                        itemFilterViewModel.itemList,
                                        key = { System.identityHashCode(it) }
                                    ) {
                                        itemSlot.invoke(
                                            it,
                                            itemFilterViewModel.itemListLayoutStyle,
                                            itemFilterViewModel.orderByType
                                        )
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}