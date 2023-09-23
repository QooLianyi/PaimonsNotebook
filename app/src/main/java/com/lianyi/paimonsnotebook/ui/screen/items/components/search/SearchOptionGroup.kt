package com.lianyi.paimonsnotebook.ui.screen.items.components.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.screen.items.data.SearchOptionData

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchOptionGroup(
    name: String,
    options: List<SearchOptionData>,
    getOptionSelectState: (SearchOptionData)->Boolean,
    onSelectedItem: (SearchOptionData) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)

        Spacer(modifier = Modifier.height(2.dp))

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            options.forEach { option ->
                Box(modifier = Modifier.padding(top = 6.dp)) {
                    SearchOption(option, getOptionSelectState(option), onSelectedItem)
                }
            }
        }
    }
}