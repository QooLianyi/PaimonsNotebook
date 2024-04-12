package com.lianyi.paimonsnotebook.ui.screen.home.components.card.daily_note_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.data.hoyolab.game_record.DailyNote
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.drawArcBorder
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.theme.Primary
import com.lianyi.paimonsnotebook.ui.theme.Primary_7
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
internal fun DailyNoteCard(
    item: DailyNote,
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PrimaryText(text = "实时便笺", fontSize = 16.sp)

            Text(
                text = "${item.role.nickname} | ${item.role.region_name} | Lv.${item.role.level}",
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .radius(8.dp)
                    .size(152.dp)
                    .background(White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.icon_resin),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp)
                        .drawArcBorder(
                            color = Primary,
                            enabledTrack = true,
                            trackColor = Primary_7,
                            sweepAngle = 300f,
                            lineWidth = 22f
                        )
                        .padding(20.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                PrimaryText(
                    text = "${item.dailyNoteEntity.dailyNote.current_resin}/${item.dailyNoteEntity.dailyNote.max_resin}",
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            val dailyNoteText = remember(item.dailyNoteEntity.dailyNote.current_resin) {
                with(item.dailyNoteEntity.dailyNote) {
                    if (is_extra_task_reward_received) {
                        "已完成"
                    } else {
                        if (finished_task_num == total_task_num){
                            "待领取"
                        }else{
                            "${finished_task_num}/${total_task_num}"
                        }
                    }
                }
            }

            val transformerText =
                with(item.dailyNoteEntity.dailyNote){
                    if (transformer.recovery_time.reached) {
                        "准备完成"
                    } else {
                        with(StringBuilder()) {
                            val day = transformer.recovery_time.Day
                            val hour = transformer.recovery_time.Hour
                            val minute = transformer.recovery_time.Minute
                            val second = transformer.recovery_time.Second

                            if (day != 0) {
                                append("${day}天")
                            }
                            if (hour != 0) {
                                append("${hour}小时")
                            }
                            if (minute != 0) {
                                append("${minute}分钟")
                            }
                            if (second != 0) {
                                append("${second}秒")
                            }
                            toString()
                        }
                    }
                }


            Column(modifier = Modifier.fillMaxWidth()) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DailyNoteCardItemSmall(icon = R.drawable.icon_daily_task, text = dailyNoteText)
                    DailyNoteCardItemSmall(icon = R.drawable.icon_quality_convert, text = "999/999")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    DailyNoteCardItemSmall(icon = R.drawable.icon_home_coin, text = "999/999")
                    DailyNoteCardItemSmall(icon = R.drawable.icon_secret_tower, text = "999/999")
                }

            }
        }
    }
}