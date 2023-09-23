package com.lianyi.paimonsnotebook.ui.screen.setting.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.theme.*

@Composable
fun SettingOption_De(
    title: String,
    description: String,
    onClick: () -> Unit,
    slot: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .clickable {
                onClick()
            }
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                color = Black,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = Black_70
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        slot()
    }
}