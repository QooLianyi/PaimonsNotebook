package com.lianyi.paimonsnotebook.common.components.blur_card.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.modifier.widget.blur_card.blurCard

@Composable
fun BlurCard(
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .blurCard(),
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        content()
    }
}