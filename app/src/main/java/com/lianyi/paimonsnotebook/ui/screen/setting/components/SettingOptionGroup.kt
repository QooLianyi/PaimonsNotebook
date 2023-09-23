package com.lianyi.paimonsnotebook.ui.screen.setting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.screen.setting.data.OptionListData
import com.lianyi.paimonsnotebook.ui.theme.Primary_2

@Composable
fun SettingOptionGroup(
    groupName: String,
    list: List<OptionListData>,
) {
    Column {

        Text(
            text = groupName, fontSize = 16.sp, color = Primary_2,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            list.forEach { item ->
                SettingOption(
                    title = item.name,
                    description = item.description,
                    onClick = item.onClick,
                    slot = item.slot
                )
            }
        }
    }
}