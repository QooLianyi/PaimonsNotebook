package com.lianyi.paimonsnotebook.ui.screen.achievement.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.layout.ShowIf
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingLayout
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.media.NetworkImage
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPagePlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.core.ui.components.text.InfoText
import com.lianyi.paimonsnotebook.common.components.widget.InputTextFiled
import com.lianyi.paimonsnotebook.common.components.widget.button.TitleAndDescriptionActionButton
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.color.alpha
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.achievement.viewmodel.AchievementScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.BlurButton
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.BlurCardBackgroundColor
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor_Light_1
import com.lianyi.paimonsnotebook.ui.theme.Font_Normal
import com.lianyi.paimonsnotebook.ui.theme.Font_Primary
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.White_40

class AchievementScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[AchievementScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme(this) {

                ContentLoadingLayout(
                    loadingState = viewModel.loadingState,
                    loadingContent = {
                        ContentLoadingPlaceholder()
                    },
                    emptyContent = {
                        EmptyPagePlaceholder(title = "当前没有成就记录用户") {
                            TitleAndDescriptionActionButton(
                                title = "前往成就记录设置页",
                                description = "前往成就记录设置界面添加一个成就记录用户",
                                modifier = Modifier
                                    .padding(16.dp, 0.dp)
                                    .fillMaxWidth(),
                                onClick = viewModel::goOptionScreen
                            )
                        }
                    },
                    errorContent = {
                        ErrorPlaceholder {}
                    }, defaultContent = {
                        ErrorPlaceholder {}
                    }
                ) {
                    AchievementContent()
                }
            }
        }
    }

    @Composable
    private fun AchievementContent() {
        Box(modifier = Modifier.fillMaxSize()) {

            //最底层列表
            ContentSpacerLazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(36.dp))
                }

                //当显示详情信息的时候在顶部添加一个空占位
                if (viewModel.showDetailInfo) {
                    item {
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }

                items(viewModel.achievementGoalList) { item ->
                    Column(
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .radius(8.dp)
                            .fillMaxWidth()
                            .background(CardBackGroundColor_Light_1)
                            .clickable {
                                viewModel.onClickGoalItem(item.goal.id)
                            }
                            .padding(8.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            NetworkImage(
                                url = item.goal.iconUrl, modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(White_40)
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = item.goal.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                com.lianyi.core.ui.components.text.InfoText(
                                    text = "${item.finishCount}/${item.total}",
                                    fontSize = 10.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = item.finishCount.toFloat() / item.total,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp)
                        )
                    }
                }
            }

            //搜索结果
            ShowIf(show = viewModel.inputTextValue.isNotBlank()) {
                ContentSpacerLazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackGroundColor),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(36.dp))
                    }

                    //当显示详情信息的时候在顶部添加一个空占位
                    if (viewModel.showDetailInfo) {
                        item {
                            Spacer(modifier = Modifier.height(64.dp))
                        }
                    }

                    if (viewModel.resultList.isEmpty()) {
                        item {
                            Row {
                                Image(
                                    painter = painterResource(id = R.drawable.emotion_icon_nahida_thinking),
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = "没有找到相关的成就,换个关键词试试吧!",
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    //结果数据列表
                    items(viewModel.resultList) { item ->
                        Column(modifier = Modifier
                            .radius(2.dp)
                            .fillMaxWidth()
                            .clickable {
                                viewModel.onClickSearchResultItem(item)
                            }
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.title,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            com.lianyi.core.ui.components.text.InfoText(
                                text = item.description,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            //顶层功能区
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackGroundColor)
            ) {
                StatusBarPaddingSpacer()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 0.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    BlurButton(
                        resId = R.drawable.ic_star_cup,
                        onClick = viewModel::toggleDetailInfo
                    )

                    InputTextFiled(
                        value = viewModel.inputTextValue,
                        onValueChange = viewModel::onInputTextValueChange,
                        backgroundColor = BlurCardBackgroundColor,
                        borderRadius = 2.dp,
                        modifier = Modifier
                            .height(36.dp)
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart,
                        padding = PaddingValues(8.dp, 5.dp, 5.dp, 5.dp),
                        placeholder = "根据名称、描述、版本号搜索成就",
                        maxLines = 1
                    )

                    if (viewModel.inputTextValue.isNotBlank()) {
                        BlurButton(
                            resId = R.drawable.ic_dismiss_circle_full,
                            onClick = viewModel::onClearInputText
                        )
                    }

                    BlurButton(
                        resId = R.drawable.ic_navigation,
                        onClick = viewModel::goOptionScreen
                    )
                }

                //成就详情
                AnimatedVisibility(visible = viewModel.showDetailInfo) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .pointerInput(Unit) {}
                            .padding(top = 0.dp, bottom = 8.dp, start = 16.dp, end = 16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {

                        val personTextContent = remember(viewModel.processPercent) {
                            "%.2f".format(viewModel.processPercent * 100)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${viewModel.selectedUser?.name}的成就记录",
                                fontSize = 14.sp,
                                color = Font_Normal
                            )

                            Text(
                                text = "专辑进度:${viewModel.achievementGoalFinishCount}/${viewModel.achievementGoalCount}",
                                fontSize = 12.sp,
                                color = Font_Normal
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text(
                                text = "${personTextContent}%",
                                fontSize = 20.sp,
                                color = Font_Primary,
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "${viewModel.achievementFinishCount}/${viewModel.achievementsCount}",
                                fontSize = 16.sp,
                                color = Font_Normal
                            )
                        }

                        LinearProgressIndicator(
                            progress = viewModel.processPercent,
                            strokeCap = StrokeCap.Round,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(4.dp),
                            color = GachaStar5Color,
                            backgroundColor = GachaStar5Color.alpha(.2f)
                        )

                    }
                }
            }
        }
    }
}