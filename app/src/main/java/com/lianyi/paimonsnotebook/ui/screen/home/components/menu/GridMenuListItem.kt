package com.lianyi.paimonsnotebook.ui.screen.home.components.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.home.data.FunctionItemData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
internal fun GridMenuListItem(
    item: FunctionItemData,
    block: (FunctionItemData) -> Unit,
) {

    Column(
        Modifier
            .radius(4.dp)
            .background(White)
            .size(64.dp)
            .clickable {
                block(item)
            }
            .padding(0.dp, 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.name,
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(Black)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.name,
            fontSize = 13.sp,
            color = Black,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}