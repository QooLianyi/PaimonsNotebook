package com.lianyi.paimonsnotebook.ui.screen.home.components.card.event

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.hk4e.announcement.AnnouncementData
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor

@Composable
internal fun EventCardGroup(
    groupName: String,
    list: List<AnnouncementData.AnnouncementList.AnnouncementItem>,
) {

    Column(
        modifier = Modifier
            .radius(8.dp)
            .background(CardBackGroundColor)
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        PrimaryText(
            text = groupName, fontSize = 16.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            list.forEach {
                EventCard(it)
            }
        }

//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier.height(130.dp * (list.size / 2 + list.size % 2)),
//            horizontalArrangement = Arrangement.spacedBy(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//
//        }

    }

}