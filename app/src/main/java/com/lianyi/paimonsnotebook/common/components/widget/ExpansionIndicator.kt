package com.lianyi.paimonsnotebook.common.components.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black_60

/*
* 展开指示器
* 用于表示可折叠项是否处于展开状态
* */
@Composable
fun ExpansionIndicator(
    expand: Boolean,
    size: Dp = 24.dp,
    onClick: () -> Unit
) {
    val dropdownAnim by animateFloatAsState(
        targetValue = if (expand) 180f else 0f,
        label = ""
    )

    Icon(painter = painterResource(id = R.drawable.ic_chevron_down),
        contentDescription = null,
        tint = Black_60,
        modifier = Modifier
            .radius(2.dp)
            .size(size)
            .clickable(onClick = onClick)
            .rotate(dropdownAnim)
    )
}