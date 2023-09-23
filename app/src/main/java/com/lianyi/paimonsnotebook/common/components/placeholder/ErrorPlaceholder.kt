package com.lianyi.paimonsnotebook.common.components.placeholder

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R


@Composable
fun ErrorPlaceholder(
    text: String = "出错了...",
    content: @Composable () -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.emotion_icon_paimon_error),
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier
        )

        content.invoke()
    }
}