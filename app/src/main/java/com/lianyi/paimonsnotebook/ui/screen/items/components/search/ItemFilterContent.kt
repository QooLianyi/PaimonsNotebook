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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.util.enums.ListLayoutStyle
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.BlurButton
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.filter.ItemFilterViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black_60
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
                    placeholder = {
                        Text(text = "按名称筛选", fontSize = 14.sp, color = Black_60)
                    }
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
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

                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(BackGroundColor),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(8.dp),
                                    state = itemFilterViewModel.lazyListState
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
                                    item {
                                        NavigationPaddingSpacer()
                                    }
                                }
                            }

                            ListLayoutStyle.GridVertical -> {
                                LazyVerticalGrid(
                                    columns = GridCells.Adaptive(60.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(8.dp),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(BackGroundColor),
                                    state = itemFilterViewModel.lazyGridState
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
                                    item {
                                        NavigationPaddingSpacer()
                                    }
                                }
                            }

                            else -> {}
                        }

                    }
                }
            }

            NavigationPaddingSpacer()
        }
    }
}