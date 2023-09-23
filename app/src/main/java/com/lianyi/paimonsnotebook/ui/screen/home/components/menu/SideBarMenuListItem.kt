package com.lianyi.paimonsnotebook.ui.screen.home.components.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_90

@Composable
internal fun SideBarMenuListItem(
    item: ModalItemData,
    block: (ModalItemData) -> Unit,
) {
    Row(
        modifier = Modifier
            .radius(8.dp)
            .fillMaxWidth()
            .height(42.dp)
            .clickable {
                block(item)
            }
            .padding(8.dp,4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = item.icon),
            contentDescription = item.name,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Black_90)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = item.name,
            fontSize = 14.sp,
            color = Black,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}