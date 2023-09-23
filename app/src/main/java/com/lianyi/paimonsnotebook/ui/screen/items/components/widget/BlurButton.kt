package com.lianyi.paimonsnotebook.ui.screen.items.components.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius


@Composable
internal fun BlurButton(
    resId:Int,
    onClick:()->Unit
) {
    Icon(
        painter = painterResource(id = resId),
        contentDescription = null,
        modifier = Modifier
            .radius(4.dp)
            .size(36.dp)
            .clickable {
                onClick.invoke()
            }
            .padding(4.dp)
    )
}