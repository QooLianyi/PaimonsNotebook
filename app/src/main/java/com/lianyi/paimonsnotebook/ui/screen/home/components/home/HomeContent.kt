package com.lianyi.paimonsnotebook.ui.screen.home.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.LazyBanner
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.database.disk_cache.util.DiskCacheDataType
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.NearActivityData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.OfficialRecommendedPostsData
import com.lianyi.paimonsnotebook.common.web.hoyolab.bbs.WebHomeData
import com.lianyi.paimonsnotebook.ui.screen.home.components.WebHomeNearActivity
import com.lianyi.paimonsnotebook.ui.screen.home.components.BannerItem
import com.lianyi.paimonsnotebook.ui.screen.home.components.notice.HomeEventNotice
import com.lianyi.paimonsnotebook.ui.screen.home.util.PostType
import com.lianyi.paimonsnotebook.ui.theme.White

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun HomeContent(
    bannerList: List<WebHomeData.Carousel>,
    nearActivity: List<NearActivityData.Hots.Group2.Children.NearActivity>,
    noticeList: List<OfficialRecommendedPostsData.OfficialRecommendedPost>,
    goPostDetail: (String, PostType) -> Unit,
) {
    Box {

        //顶部背景
        Image(
            painter = painterResource(id = R.drawable.bg_home_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            contentScale = ContentScale.Crop
        )

        LazyColumn(Modifier.fillMaxSize()) {
            item {
                Spacer(modifier = Modifier.height(1.dp))
            }
            //轮播图
            item {
                LazyBanner(
                    bannerData = bannerList,
                    contentPaddingValues = PaddingValues(30.dp, 0.dp),
                    modifier = Modifier
                        .height(200.dp),
                    enableAutoLoop = true,
                    itemSpacing = (-30).dp
                ) { item, index, pageCurrent ->
                    BannerItem(
                        index = index,
                        pageCurrent = pageCurrent,
                        item = item,
                        diskCache = DiskCache(
                            url = item.cover,
                            name = "轮播图",
                            createFrom = "首页",
                            description = "首页顶部轮播图",
                            type = DiskCacheDataType.Temp,
                            lastUseFrom = "首页"
                        )
                    ) {
                        goPostDetail(it.path, PostType.Banner)
                    }
                }
            }


            //近期活动
            if(nearActivity.isNotEmpty()){
                item {
                    TitleText(
                        text = "近期活动",
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(White)
                            .padding(8.dp)
                    )
                }

                items(nearActivity) {
                    WebHomeNearActivity(
                        item = it,
                        diskCache = DiskCache(
                            url = it.icon,
                            name = "近期活动图片",
                            createFrom = "首页",
                            description = "${it.title},${it.abstract}",
                            type = DiskCacheDataType.Temp,
                            lastUseFrom = "首页"
                        )
                    ) { nearActivity ->
                        goPostDetail(nearActivity.url, PostType.Notice)
                    }
                }
            }

            //公告列表
            item {
                TitleText(
                    text = "公告",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .padding(8.dp)
                )
            }

            items(noticeList) {
                HomeEventNotice(
                    item = it,
                    modifier = Modifier
                        .background(White)
                        .padding(8.dp, 4.dp),
                    diskCache = DiskCache(
                        url = it.banner,
                        name = "公告列表图片",
                        createFrom = "首页",
                        description = it.subject,
                        type = DiskCacheDataType.Temp,
                        lastUseFrom = "首页"
                    )
                ) { data ->
                    goPostDetail(data.post_id, PostType.Notice)
                }
            }

            item {
                NavigationPaddingSpacer()
            }
        }

    }
}