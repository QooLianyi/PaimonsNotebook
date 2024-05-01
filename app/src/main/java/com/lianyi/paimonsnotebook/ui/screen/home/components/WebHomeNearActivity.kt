package com.lianyi.paimonsnotebook.ui.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.NearActivityData
import com.lianyi.paimonsnotebook.ui.theme.Gray_97
import com.lianyi.paimonsnotebook.ui.theme.Gray_F5
import com.lianyi.paimonsnotebook.ui.theme.Primary_4
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
internal fun WebHomeNearActivity(
    item: NearActivityData.Hots.Group2.Children.NearActivity,
    diskCache: DiskCache,
    block: (url: String) -> Unit,
) {
    Box(
        Modifier
            .fillMaxWidth()
            .background(White)
            .padding(8.dp, 3.dp)
            .radius(4.dp)
            .clickable {
                block(item.url)
            }
            .background(Gray_F5)) {
        Row(
            modifier = Modifier
                .radius(4.dp)
                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            NetworkImage(
                url = item.icon,
                modifier = Modifier
                    .radius(4.dp)
                    .size(40.dp),
                contentScale = ContentScale.Crop,
                diskCache = diskCache
            )

            Column(
                Modifier
                    .weight(1f)
                    .padding(10.dp, 0.dp, 0.dp, 0.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 14.sp
                )
                Text(
                    text = item.abstract,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 13.sp,
                    color = Gray_97
                )
            }

        }

        if (item.end_time != "0") {
            val timeText =
                "还剩${TimeHelper.timeStampParseToTextDayAndHour((item.end_time.toLongOrNull() ?: 0L) - System.currentTimeMillis())}"

            Text(
                text = timeText,
                fontSize = 10.sp,
                color = White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .background(Primary_4, RoundedCornerShape(0.dp, 4.dp, 0.dp, 4.dp))
                    .padding(4.dp, 1.dp)
            )
        }

    }
}