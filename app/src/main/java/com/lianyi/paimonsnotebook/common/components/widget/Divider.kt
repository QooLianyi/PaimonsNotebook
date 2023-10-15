package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.ui.theme.Black_10

@Composable
fun Divider_10() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(.5.dp)
        .background(Black_10))
}