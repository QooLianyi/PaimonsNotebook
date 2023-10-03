package com.lianyi.paimonsnotebook.ui.screen.home.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.data.hoyolab.game_record.DailyNote
import com.lianyi.paimonsnotebook.common.database.gacha.data.GachaRecordOverview
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement.AnnouncementData
import com.lianyi.paimonsnotebook.ui.screen.home.components.card.daily_note_card.DailyNoteCardGroup
import com.lianyi.paimonsnotebook.ui.screen.home.components.card.event.EventCardGroup
import com.lianyi.paimonsnotebook.ui.screen.home.components.card.gacha_record.GachaRecordCardGroup
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1

@Composable
internal fun HomeContentSimple(
    dailyNoteOverviewList: List<DailyNote>,
    gachaRecordOverviewList: List<GachaRecordOverview>,
    gachaEventList: List<AnnouncementData.AnnouncementList.AnnouncementItem>,
    eventList: List<AnnouncementData.AnnouncementList.AnnouncementItem>,
    onNavigationClick: () -> Unit,
) {
    ContentSpacerLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackGroundColor), contentPadding = PaddingValues(12.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        //头部信息区
        item {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "早上好", fontSize = 16.sp)

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "测试用户名称123456123456879",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Image(
                    painter = painterResource(id = R.drawable.icon_klee_square),
                    contentDescription = null,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .size(65.dp)
                )
            }
        }

        //信息提示卡
        item {

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier
                .radius(8.dp)
                .fillMaxWidth()
                .background(CardBackGroundColor_Light_1)
                .clickable {

                }
                .padding(12.dp, 8.dp)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.icon_klee_square),
                    contentDescription = null,
                    modifier = Modifier
                        .radius(4.dp)
                        .size(50.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = "提示信息是用于提示本软件中的各个功能的用法与开发过程中各种有趣的内容，可以在设置中关闭提示信息。",
                    fontSize = 16.sp
                )

            }

        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        //信息卡片列表
        item {
            Text(
                text = "信息卡片",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Black
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        //实时便笺
        item {
            DailyNoteCardGroup(dailyNoteOverviewList)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        //祈愿记录
        item {
            GachaRecordCardGroup(gachaRecordOverviewList = gachaRecordOverviewList)
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }

        //祈愿活动
        if (eventList.isNotEmpty()) {
            item {
                EventCardGroup("活动", eventList)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        //活动
        if (gachaEventList.isNotEmpty()) {
            item {
                EventCardGroup("祈愿活动", gachaEventList)
            }
        }
    }

}