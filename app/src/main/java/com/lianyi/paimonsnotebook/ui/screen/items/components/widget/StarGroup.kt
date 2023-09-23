package com.lianyi.paimonsnotebook.ui.screen.items.components.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color

@Composable
fun StarGroup(
    starCount:Int,
    modifier: Modifier = Modifier,
    starTint:Color = GachaStar5Color,
    starSize: Dp = 18.dp,
    horizontalArrangement:Arrangement.Horizontal = Arrangement.SpaceAround
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        repeat(starCount) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = null,
                modifier = Modifier
                    .size(starSize),
                tint = starTint
            )
        }
    }
}