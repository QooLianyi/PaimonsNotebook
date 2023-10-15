package com.lianyi.paimonsnotebook.ui.screen.daily_note.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.common.database.disk_cache.entity.DiskCache
import com.lianyi.paimonsnotebook.common.extension.modifier.action.pressureMonitor
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.drawArcBorder
import com.lianyi.paimonsnotebook.common.extension.modifier.animation.shake
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.extension.value.nonScaledSpPX
import com.lianyi.paimonsnotebook.common.extension.value.toDp
import com.lianyi.paimonsnotebook.common.extension.value.toPx
import com.lianyi.paimonsnotebook.common.util.compose.shape.CirclePath
import com.lianyi.paimonsnotebook.common.util.compose.shape.CirclePathStartPoint
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.binding.UserGameRoleData
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.daily_note.DailyNoteData
import com.lianyi.paimonsnotebook.ui.screen.daily_note.data.DailyNoteCardItemData
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1
import com.lianyi.paimonsnotebook.ui.theme.Error
import com.lianyi.paimonsnotebook.ui.theme.Success
import com.lianyi.paimonsnotebook.ui.theme.Success_1

@Composable
fun DailyNoteCard(
    userGameRole: UserGameRoleData.Role,
    dailyNoteData: DailyNoteData,
    modifier: Modifier = Modifier,
    onClickSettings: () -> Unit = {},
    onDelete: () -> Unit = {},
    diskCache: DiskCache = DiskCache(""),
) {

    val currentTime = remember(dailyNoteData.resin_recovery_time) {
        System.currentTimeMillis()
    }

    var showMoreInfo by remember {
        mutableStateOf(false)
    }

    var pressedDelete by remember {
        mutableStateOf(false)
    }

    val deleteAnim = remember {
        Animatable(0f, Float.VectorConverter)
    }

    val dailyTaskDescriptionText = remember(dailyNoteData.finished_task_num) {
        if (dailyNoteData.is_extra_task_reward_received) {
            "已领取「每日委托」奖励"
        } else {
            if (dailyNoteData.total_task_num == dailyNoteData.finished_task_num) {
                "「每日委托」奖励待领取"
            } else {
                "「每日委托」完成数量不足"
            }
        }
    }

    val homeCoinDescription = remember(dailyNoteData.home_coin_recovery_time) {
        val homeIconRecoveryTime =
            dailyNoteData.home_coin_recovery_time.toLongOrNull() ?: 0L

        if (dailyNoteData.current_home_coin == dailyNoteData.max_home_coin) {
            "洞天宝钱已到达上限"
        } else {
            "将在${
                TimeHelper.getRecoverTime(
                    homeIconRecoveryTime,
                    currentTime
                )
            }时到达上限"
        }
    }

    val transformerDescription =
        remember(dailyNoteData.transformer.recovery_time.reached) {
            if (dailyNoteData.transformer.recovery_time.reached) {
                "准备完成"
            } else {
                with(StringBuilder()) {
                    val day = dailyNoteData.transformer.recovery_time.Day
                    val hour = dailyNoteData.transformer.recovery_time.Hour
                    val minute = dailyNoteData.transformer.recovery_time.Minute
                    val second = dailyNoteData.transformer.recovery_time.Second

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
                    append("后准备完成")
                    toString()
                }
            }
        }


    val list = remember(dailyNoteData.resin_recovery_time) {
        listOf(
            DailyNoteCardItemData(
                R.drawable.icon_daily_task,
                "每日委托",
                dailyTaskDescriptionText,
                "${dailyNoteData.finished_task_num}/${dailyNoteData.total_task_num}",
                getCircleArcAngle(
                    dailyNoteData.finished_task_num,
                    dailyNoteData.total_task_num
                )
            ),
            DailyNoteCardItemData(
                R.drawable.icon_home_coin,
                "洞天宝钱",
                homeCoinDescription,
                "${dailyNoteData.current_home_coin}/${dailyNoteData.max_home_coin}",
                getCircleArcAngle(
                    dailyNoteData.current_home_coin,
                    dailyNoteData.max_home_coin
                )
            ),
            DailyNoteCardItemData(
                R.drawable.icon_secret_tower,
                "值得铭记的强敌",
                "剩余减半次数",
                "${dailyNoteData.remain_resin_discount_num}/${dailyNoteData.resin_discount_num_limit}",
                getCircleArcAngle(
                    dailyNoteData.remain_resin_discount_num,
                    dailyNoteData.resin_discount_num_limit
                )
            ),
            DailyNoteCardItemData(
                R.drawable.icon_quality_convert,
                "参量质变仪",
                transformerDescription,
                if (dailyNoteData.transformer.recovery_time.reached) "可使用" else "准备中",
                getCircleArcAngle(
                    0,
                    0,
                    dailyNoteData.transformer.recovery_time.reached
                )
            )
        )
    }

    LaunchedEffect(deleteAnim.value) {
        if (deleteAnim.value >= 1f) {
            deleteAnim.snapTo(0f)
            pressedDelete = false
            onDelete.invoke()
        }
    }

    LaunchedEffect(pressedDelete) {
        if (pressedDelete) {
            deleteAnim.animateTo(1f, tween(2000))
        } else if (deleteAnim.value > 0f) {
            deleteAnim.animateTo(0f, tween(2000))
        }
    }

    Box(modifier = modifier
        .radius(6.dp)
        .fillMaxWidth()
        .background(CardBackGroundColor_Light_1)
        .clickable {
            showMoreInfo = !showMoreInfo
        }
        .padding(10.dp, 12.dp)
    ) {

        //基础信息
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Error, CirclePath(
                        deleteAnim.value,
                        CirclePathStartPoint.TopEnd,
                        offset = Offset(-38.5f, 38.5f)
                    )
                )
        ) {

            Row {
                TitleText(
                    text = "${userGameRole.nickname} | ${userGameRole.region_name} | Lv.${userGameRole.level}",
                    modifier = Modifier.weight(1f)
                )

                Icon(painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .clickable {
                            onClickSettings()
                        })

                Spacer(modifier = Modifier.width(10.dp))

                Icon(painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp)
                        .shake(pressedDelete)
                        .pressureMonitor(
                            onBegin = {
                                pressedDelete = true
                            }, onEnd = {
                                pressedDelete = false
                            })
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val resinItemData = remember(dailyNoteData.current_resin) {
                val resinRecoveryTime = dailyNoteData.resin_recovery_time.toLongOrNull() ?: 0L

                val resinDescriptionText = when (dailyNoteData.current_resin) {
                    dailyNoteData.max_resin -> "原粹树脂已到达上限"
                    else -> "将在${
                        TimeHelper.getRecoverTime(
                            resinRecoveryTime,
                            currentTime
                        )
                    }到达上限"
                }

                DailyNoteCardItemData(
                    R.drawable.icon_resin,
                    "原粹树脂",
                    resinDescriptionText,
                    "${dailyNoteData.current_resin}/${dailyNoteData.max_resin}",
                    getCircleArcAngle(dailyNoteData.current_resin, dailyNoteData.max_resin)
                )
            }

            DailyNoteCardInfoItem(data = resinItemData)

            AnimatedVisibility(visible = showMoreInfo) {

                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    list.forEach { dailyNoteCardItemData ->
                        DailyNoteCardInfoItem(dailyNoteCardItemData)
                    }

                    //探索
                    if (dailyNoteData.expeditions.isNotEmpty()) {

                        val extraHeight = (12.sp.toPx() - 12.nonScaledSpPX()).sp.toDp()

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp + extraHeight)
                        ) {
                            items(dailyNoteData.expeditions) { item ->
                                Column(
                                    modifier = Modifier.width(36.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    NetworkImage(
                                        url = item.avatar_side_icon,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .padding(1.dp)
                                            .drawArcBorder(
                                                color = if ((item.remained_time.toIntOrNull()
                                                        ?: -1) == 0
                                                ) Success else Success_1,
                                                sweepAngle = 360f
                                            )
                                            .offset(y = (-3).dp),
                                        diskCache = diskCache.copy(url = item.avatar_side_icon)
                                    )

                                    Spacer(modifier = Modifier.height(3.dp))

                                    Text(
                                        text = if ((item.remained_time.toLongOrNull()
                                                ?: 0L) == 0L
                                        ) "已完成" else "探索中",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getCircleArcAngle(current: Int = 0, max: Int = 0, maxValue: Boolean = false): Float {
    return if (maxValue) {
        360f
    } else {
        val mCurrent = current.toFloat()
        val mMax = max.toFloat()

        (mCurrent / mMax * 360f).takeUnless { it.isNaN() } ?: 0f
    }
}