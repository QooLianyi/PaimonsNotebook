package com.lianyi.paimonsnotebook.ui.screen.home.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.WebHomeData

@Composable
internal fun BannerItem(
    index: Int,
    pageCurrent: Int,
    item: WebHomeData.Carousel,
    diskCache: DiskCache,
    block: (WebHomeData.Carousel) -> Unit,
) {
    val animScale by animateFloatAsState(targetValue = if (pageCurrent == index) 1f else .8f,
        label = ""
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .scale(animScale),
        contentAlignment = Alignment.Center,
    ) {
        NetworkImage(
            url = item.cover, modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clickable {
                    block(item)
                }, contentScale = ContentScale.FillWidth,
            diskCache = diskCache
        )
    }
}