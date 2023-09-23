package com.lianyi.paimonsnotebook.ui.screen.gacha.components.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.drawArcBorder
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingEnd
import com.lianyi.paimonsnotebook.ui.theme.*

@Composable
fun GachaRecordCard_De() {

    BoxWithConstraints {

        Column(
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(6.dp))
                .fillMaxWidth()
                .background(White)
                .padding(10.dp)
        ) {

            val infoSpace = remember {
                2.dp
            }

            var showDetailInfo by remember {
                mutableStateOf(true)
            }

            val scrollState = rememberLazyListState()

            LaunchedEffect(showDetailInfo) {
                if (showDetailInfo) {
                    scrollState.animateScrollToItem(0)
                }
            }

            val showMoreInfoAngle by animateFloatAsState(targetValue = if (showDetailInfo) 0f else 180f)

            Row(verticalAlignment = Alignment.CenterVertically) {

                TitleText(text = "角色活动", modifier = Modifier.weight(1f), fontSize = 22.sp)

                Icon(
                    painter = painterResource(id = R.drawable.ic_list),
                    contentDescription = null,
                    modifier = Modifier
                        .paddingEnd(10.dp)
                        .size(30.dp),
                    tint = Info
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_up),
                    contentDescription = null, modifier = Modifier
                        .size(30.dp)
                        .rotate(showMoreInfoAngle)
                        .clickable {
                            showDetailInfo = !showDetailInfo
                        }, tint = Info
                )
            }

            AnimatedVisibility(showDetailInfo) {

                Column(modifier = Modifier) {

                    Spacer(
                        modifier = Modifier
                            .padding(0.dp, 4.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Primary_9)
                    )

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "200000", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(text = "抽", fontSize = 14.sp, modifier = Modifier.offset(y = (-5).dp))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .drawArcBorder(
                                        GachaStar5Color,
                                        sweepAngle = 200f,
                                        enabledTrack = true,
                                        trackColor = Warning_1
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Text(text = "99", color = GachaStar5Color, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(text = "五星", color = GachaStar5Color, fontSize = 16.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .drawArcBorder(
                                        GachaStar4Color,
                                        sweepAngle = 200f,
                                        enabledTrack = true,
                                        trackColor = Warning_1
                                    ), contentAlignment = Alignment.Center
                            ) {
                                Text(text = "99", color = GachaStar4Color, fontSize = 14.sp)
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(text = "四星", color = GachaStar4Color, fontSize = 16.sp)
                        }

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "2022-2-2 12:00 ~ 2022-2-2 12:00 ", color = Info, fontSize = 14.sp)

                    Spacer(
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Primary_9)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "五星", color = GachaStar5Color, fontSize = 16.sp)
                        Text(text = "999999999 [5.55%]", color = GachaStar5Color, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(infoSpace))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "四星", color = GachaStar4Color, fontSize = 16.sp)
                        Text(text = "999999999 [5.55%]", color = GachaStar4Color, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(infoSpace))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "三星", color = GachaStar3Color, fontSize = 16.sp)
                        Text(text = "999999999 [5.55%]", color = GachaStar3Color, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(infoSpace))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "平均五星抽数", color = Black, fontSize = 16.sp)
                        Text(text = "999抽", color = Primary_1, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(infoSpace))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "UP平均抽数", color = Black, fontSize = 16.sp)
                        Text(text = "999抽", color = Primary_1, fontSize = 16.sp)
                    }

                    Spacer(
                        modifier = Modifier
                            .padding(0.dp, 10.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Primary_9)
                    )

                }
            }

            AnimatedVisibility(visible = !showDetailInfo) {

                LazyColumn(
                    modifier = Modifier.requiredHeightIn(
                        0.dp,
                        if (showDetailInfo) 200.dp else 500.dp
                    ),
                    userScrollEnabled = !showDetailInfo,
                    state = scrollState
                ) {

                    items(10) {

                        Row(
                            modifier = Modifier.padding(4.dp, 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.icon_klee_square),
                                contentDescription = "",
                                modifier = Modifier.size(50.dp)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "角色名称占位",
                                color = Primary_1,
                                fontSize = 16.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "保底",
                                color = Primary_1,
                                fontSize = 16.sp,
                                modifier = Modifier.paddingEnd(8.dp)
                            )

                            Text(
                                text = "UP",
                                color = GachaStar5Color,
                                fontSize = 16.sp,
                                modifier = Modifier.paddingEnd(8.dp)
                            )

                            Text(text = "999", color = Black, fontSize = 16.sp)

                        }

                    }

                }

            }


        }


    }

}