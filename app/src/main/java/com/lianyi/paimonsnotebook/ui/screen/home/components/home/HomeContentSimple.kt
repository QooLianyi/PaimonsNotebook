package com.lianyi.paimonsnotebook.ui.screen.home.components.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData


@Composable
internal fun HomeContentSimple(
    list: List<ModalItemData>,
    block: (ModalItemData) -> Unit,
) {
    val list3 = mutableListOf<List<ModalItemData>>()
    var listcount = list.size/3
    if(list.size%3!=0)
        listcount+=1
    for (i in 0 until listcount){
        val list2 = mutableListOf<ModalItemData>()
        list2.add(list[i*3])
        list2.add(list[i*3+1])
        list2.add(list[i*3+2])
        list3.add(list2)
    }

    ContentSpacerLazyColumn(Modifier.fillMaxSize(), contentPadding = PaddingValues(12.dp)) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
        }
        items(list3) {data ->
            SimpleHomeGrid(data) {
                    block(it)
            }
        }
    }
}