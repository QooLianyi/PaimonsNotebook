package com.lianyi.paimonsnotebook.ui.screen.home.components.card.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement.AnnouncementData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Black_60
import com.lianyi.paimonsnotebook.ui.theme.Info
import com.lianyi.paimonsnotebook.ui.theme.Info_1
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 首页活动卡片
* */
@Composable
internal fun EventCard(
    item: AnnouncementData.AnnouncementList.AnnouncementItem,
) {
    Column(
        modifier = Modifier
            .radius(8.dp)
    ) {
        NetworkImage(
            diskCache = DiskCache(item.banner),
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(White)
                .padding(4.dp)
        ) {
            Text(
                text = item.title,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = item.subtitle,
                fontSize = 11.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Black
            )

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${item.start_time} ~ ${item.end_time}",
                    fontSize = 8.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Info
                )
                Text(
                    text = "99天后结束",
                    fontSize = 8.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Black_60
                )
            }
        }

    }
}