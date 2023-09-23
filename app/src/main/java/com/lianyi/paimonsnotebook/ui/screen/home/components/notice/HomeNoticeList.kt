package com.lianyi.paimonsnotebook.ui.screen.home.components.notice

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.OfficialRecommendedPostsData

@Composable
internal fun HomeNoticeList(noticeList: List<OfficialRecommendedPostsData.OfficialRecommendedPost>) {
    LazyColumn(Modifier
        .padding(8.dp, 0.dp)
    ) {
        items(noticeList) { item ->
        }
    }
}