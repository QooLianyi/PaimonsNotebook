package com.lianyi.paimonsnotebook.ui.screen.items.components.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.BlurCardBackgroundColor

/*
* 物品信息卡片布局
* */
@Composable
internal fun ItemInformationCardLayout(
    margin:Dp = 10.dp,
    contentSpacer: Dp = 12.dp,
    content:@Composable ColumnScope.()->Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(margin)
            .radius(4.dp)
            .background(BlurCardBackgroundColor)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(contentSpacer),
        content = content
    )
}