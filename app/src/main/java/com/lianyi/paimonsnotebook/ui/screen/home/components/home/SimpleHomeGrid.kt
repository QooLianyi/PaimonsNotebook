package com.lianyi.paimonsnotebook.ui.screen.home.components.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData

@Composable
internal fun SimpleHomeGrid(
    list: List<ModalItemData>,
    block: (ModalItemData) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(10.dp),
    ) {
        items(list) { data ->
            SimpleHomeGridItem(data) {
                block(it)
            }
        }
    }

}