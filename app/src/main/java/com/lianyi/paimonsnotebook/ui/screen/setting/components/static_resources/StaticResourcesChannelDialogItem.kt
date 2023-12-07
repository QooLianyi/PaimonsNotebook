package com.lianyi.paimonsnotebook.ui.screen.setting.components.static_resources

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.ui.screen.setting.data.StaticResourcesChannelData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Font_Normal

@Composable
fun StaticResourcesChannelDialogItem(
    data: StaticResourcesChannelData,
    onClick:(StaticResourcesChannelData)->Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onClick.invoke(data)
        },
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(text = data.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = Black)
        Text(text = data.description, fontSize = 12.sp, color = Font_Normal)
    }
}