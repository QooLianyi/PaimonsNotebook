package com.lianyi.paimonsnotebook.ui.screen.gacha.components.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R

@Composable
fun GachaRecordLoadingPage() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = R.drawable.emotion_icon_nahida_thinking),
            contentDescription = null,
            modifier = Modifier.size(160.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "正在读取祈愿记录", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    }
}