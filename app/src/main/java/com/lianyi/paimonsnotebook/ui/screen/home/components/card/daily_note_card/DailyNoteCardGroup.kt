package com.lianyi.paimonsnotebook.ui.screen.home.components.card.daily_note_card

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.media.Indicator
import com.lianyi.paimonsnotebook.common.data.hoyolab.game_record.DailyNote
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.compose.provider.NoOverscrollEffectThemeProvides
import com.lianyi.paimonsnotebook.ui.theme.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun DailyNoteCardGroup(
    list:List<DailyNote>,
    startIndex: Int = 0,
) {
    val state = rememberPagerState(startIndex){
        list.size
    }

    val currentIndex = state.currentPage

    Column(
        modifier = Modifier
            .radius(8.dp)
            .background(CardBackGroundColor)
            .padding(0.dp, 12.dp)
    ) {
        NoOverscrollEffectThemeProvides {
            HorizontalPager(
                state = state
            ) {
                Row(modifier = Modifier.padding(12.dp, 0.dp)) {
                    DailyNoteCard(list[it])
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Indicator(
            currentPageIndex = currentIndex,
            count = state.pageCount,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            activeColor = Primary_2,
            disableColor = Primary_8
        )
    }
}