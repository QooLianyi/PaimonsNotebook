package com.lianyi.paimonsnotebook.ui.screen.home.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.home.data.ModalItemData
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_90

@Composable
internal fun SimpleHomeGridItem(
    item: ModalItemData,
    block: (ModalItemData) -> Unit,
) {
    Column(
        modifier = Modifier
            .radius(30.dp)
            .height(100.dp)
            .width(100.dp)
            .clickable {
                block(item)
            }
            .padding(8.dp,4.dp)
            .background(BackGroundColor),
        //verticalAlignment = Alignment.CenterVertically
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.name,
            modifier = Modifier.size(30.dp),
            colorFilter = ColorFilter.tint(Black_90)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 15.sp,
            color = Black,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}