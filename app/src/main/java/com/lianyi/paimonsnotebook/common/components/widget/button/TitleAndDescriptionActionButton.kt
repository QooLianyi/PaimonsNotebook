package com.lianyi.paimonsnotebook.common.components.widget.button

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.widget.IconTextHintSlotItem
import com.lianyi.paimonsnotebook.ui.theme.Black_30

@Composable
fun TitleAndDescriptionActionButton(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    IconTextHintSlotItem(
        modifier = modifier,
        title = title,
        description = description,
        primaryTextSize = 16.sp,
        secondTextSize = 14.sp,
        onClick = onClick
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_chevron_right),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Black_30
        )
    }
}