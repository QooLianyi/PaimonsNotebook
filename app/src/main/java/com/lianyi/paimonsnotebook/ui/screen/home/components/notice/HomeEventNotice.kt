package com.lianyi.paimonsnotebook.ui.screen.home.components.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.OfficialRecommendedPostsData
import com.lianyi.paimonsnotebook.ui.theme.Black_30
import com.lianyi.paimonsnotebook.ui.theme.White

/*
* 首页公告
* */
@Composable
internal fun HomeEventNotice(
    item: OfficialRecommendedPostsData.OfficialRecommendedPost,
    modifier: Modifier = Modifier,
    diskCache: DiskCache,
    block: (String) -> Unit,
) {
    Box(modifier = modifier.clickable {
        block(item.post_id)
    }) {

        NetworkImage(
            url = item.banner, modifier = Modifier
                .fillMaxWidth()
                .heightIn(0.dp, 160.dp),
            contentScale = ContentScale.FillWidth,
            diskCache = diskCache
        )

        Row(
            Modifier
                .fillMaxWidth()
                .height(35.dp)
                .background(Black_30)
                .padding(6.dp, 0.dp)
                .align(Alignment.BottomStart),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.subject,
                style = MaterialTheme.typography.body2,
                color = White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}