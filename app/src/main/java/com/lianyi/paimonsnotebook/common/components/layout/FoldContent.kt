package com.lianyi.paimonsnotebook.common.components.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.theme.Black

@Composable
fun FoldContent(
    open: Boolean,
    modifier: Modifier = Modifier,
    contentVerticalArrangement: Arrangement.Vertical = Arrangement.Top,
    contentHorizontalAlignment: Alignment.Horizontal = Alignment.Start,
    titleVerticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    titleSlot: @Composable () -> Unit,
    content: @Composable () -> Unit
) {

    val indicatorAnim by animateFloatAsState(targetValue = if (open) 180f else 0f, label = "")

    Column(
        modifier = modifier,
        verticalArrangement = contentVerticalArrangement,
        horizontalAlignment = contentHorizontalAlignment
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = titleVerticalAlignment) {
            Box(modifier = Modifier.weight(1f)) {
                titleSlot.invoke()
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_down),
                contentDescription = null,
                tint = Black,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(indicatorAnim)
            )
        }
        AnimatedVisibility(visible = open) {
            Column {
                content.invoke()
            }
        }
    }

}