package com.lianyi.paimonsnotebook.ui.screen.home.components.menu

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData

@Composable
internal fun SideBarMenuList(
    list: List<ModalItemData>,
    block: (ModalItemData) -> Unit,
) {

    LazyColumn(
        contentPadding = PaddingValues(8.dp),
    ) {
        items(list) { data ->
            SideBarMenuListItem(data) {
                block(it)
            }
        }
    }

}