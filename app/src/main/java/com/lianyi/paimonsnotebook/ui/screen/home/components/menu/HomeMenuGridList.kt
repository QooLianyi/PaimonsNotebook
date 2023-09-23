package com.lianyi.paimonsnotebook.ui.screen.home.components.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.ui.screen.home.data.FunctionItemData

@Composable
internal fun HomeMenuGridList(
    list: List<FunctionItemData>,
    block: (FunctionItemData) -> Unit,
) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(6.dp)
    ) {
        items(list) { item ->
            GridMenuListItem(item) {
                block(it)
            }
        }
    }

//    LazyColumn(modifier.padding(4.dp)) {
//
//        items(list.split(rows)) { split ->
//            Row(
//                Modifier
//                    .fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                split.forEach { item ->
//
//                    GridMenuListItem(item) {
//                        block(it)
//                    }
//
//                }
//
//                //当缺少元素，填充空白元素
//                if (split.size < rows) {
//                    val repeatTimes = rows - split.size
//                    repeat(repeatTimes) {
//                        GridMenuListItem(item = split.first(), false) {}
//                    }
//                }
//
//            }
//        }
//
//    }
}