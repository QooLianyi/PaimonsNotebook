package com.lianyi.paimonsnotebook.ui.screen.items.components.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.ui.screen.items.data.SearchOptionData
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemContentFilterHelper
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Transparent
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
internal fun OtherSortByList(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    offset: Offset,
    otherOptionList:List<SearchOptionData>,
    selectedOption: SearchOptionData,
    onClick: (Offset, SearchOptionData) -> Unit
) {
    if(expanded){
        LazyColumnDialog(title = "其他选项", onDismissRequest = onDismissRequest, content = {
            items(otherOptionList){optionData->
                val (backgroundColor, textColor) = remember(selectedOption) {
                    if (selectedOption.sortBy == optionData.sortBy) {
                        Black to White
                    } else {
                        Transparent to Black
                    }
                }

                DropdownMenuItem(
                    onClick = {
                        onClick.invoke(
                            Offset.Zero,
                            optionData
                        )
                        onDismissRequest.invoke()
                    },
                    modifier = Modifier
                        .background(backgroundColor)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Text(
                            text = ItemContentFilterHelper.getSortTypeNameByType(optionData.sortBy),
                            fontSize = 12.sp,
                            color = textColor
                        )
                    }
                }
            }
        })
    }
}