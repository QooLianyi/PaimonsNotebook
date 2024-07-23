package com.lianyi.paimonsnotebook.ui.screen.resource_manager.components.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Font_Normal
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun ImageDetailHeader(
    diskCache: DiskCache,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .pointerInput(Unit) {
            }
            .background(White)
            .padding(8.dp, 2.dp)
    ) {
        StatusBarPaddingSpacer()
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_chevron_left),
                contentDescription = null,
                tint = Black,
                modifier = Modifier
                    .radius(2.dp)
                    .size(36.dp)
                    .clickable {
                        onBack.invoke()
                    }
                    .padding(4.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                com.lianyi.core.ui.components.text.PrimaryText(
                    text = diskCache.name,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = diskCache.description,
                    fontSize = 12.sp,
                    color = Font_Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}