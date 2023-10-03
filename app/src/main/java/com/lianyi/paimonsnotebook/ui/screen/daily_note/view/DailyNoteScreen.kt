package com.lianyi.paimonsnotebook.ui.screen.daily_note.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.LazyColumnDialog
import com.lianyi.paimonsnotebook.common.components.dialog.LoadingDialog
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.text.TitleText
import com.lianyi.paimonsnotebook.common.components.widget.TextSlider
import com.lianyi.paimonsnotebook.common.extension.modifier.padding.paddingTop
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.compose.provider.NoRippleThemeProvides
import com.lianyi.paimonsnotebook.ui.screen.account.components.dialog.UserGameRolesDialog
import com.lianyi.paimonsnotebook.ui.screen.daily_note.components.DailyNoteCard
import com.lianyi.paimonsnotebook.ui.screen.daily_note.viewmodel.DailyNoteScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.Info
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class DailyNoteScreen : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[DailyNoteScreenViewModel::class.java]
    }

    @OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme(this) {

                val pullRefreshState = rememberPullRefreshState(
                    refreshing = viewModel.isRefreshing,
                    onRefresh = viewModel::refreshDailyNote
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(BackGroundColor)
                        .padding(12.dp, 0.dp)
                        .pullRefresh(pullRefreshState)
                ) {

                    Column(modifier = Modifier.fillMaxSize()) {
                        StatusBarPaddingSpacer()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .paddingTop(6.dp)
                        ) {

                            TitleText(
                                text = "实时便笺",
                                fontSize = 20.sp,
                                modifier = Modifier.weight(1f)
                            )

                            Icon(painter = painterResource(id = R.drawable.ic_phone_mobile),
                                contentDescription = null,
                                tint = Black,
                                modifier = Modifier
                                    .size(30.dp)
                                    .radius(2.dp)
                                    .clickable {
                                        viewModel.goHoyolabActivity()
                                    })

                            Spacer(modifier = Modifier.width(10.dp))

                            Icon(painter = painterResource(id = R.drawable.ic_add),
                                contentDescription = null,
                                tint = Black,
                                modifier = Modifier
                                    .size(30.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .clickable {
                                        viewModel.showSelectGameRoleDialog()
                                    })
                        }

                        ContentSpacerLazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            statusBarPaddingEnabled = false
                        ) {

                            items(viewModel.dailyNoteList, { it.role.game_uid }) {
                                DailyNoteCard(
                                    userGameRole = it.role,
                                    dailyNoteData = it.dailyNoteEntity.dailyNote,
                                    modifier = Modifier
                                        .animateItemPlacement(),
                                    onDelete = {
                                        viewModel.deleteDailyNote(it)
                                    },
                                    onClickSettings = {
                                        viewModel.showDailyNoteOptionDialog(it)
                                    },
                                    diskCache = viewModel.expeditionsAvatarIconDiskCache
                                )
                            }
                        }
                    }

                    PullRefreshIndicator(
                        refreshing = viewModel.isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

                //添加角色对话框
                if (viewModel.showSelectGameRoleDialog) {
                    UserGameRolesDialog(
                        userList = viewModel.allowAddDailyNoteUserList,
                        onButtonClick = {
                            viewModel.dismissSelectGameRoleDialog()
                        },
                        onDismissRequest = viewModel::dismissSelectGameRoleDialog,
                        onSelectRole = viewModel::onDialogSelectUserGameRole
                    )
                }

                //修改便笺对话框
                if (viewModel.showDailyNoteOptionDialog) {

                    LazyColumnDialog(
                        title = "实时便笺设置",
                        titleSpacer = 24.dp,
                        onDismissRequest = {
                            viewModel.dismissDailyNoteOptionDialog(-1)
                        },
                        onClickButton = viewModel::dismissDailyNoteOptionDialog,
                        buttons = arrayOf("确认修改", "取消")
                    ) {
                        item {

                            Column {
                                Text(
                                    text = "实时便笺中的顺序",
                                    fontSize = 18.sp,
                                    color = Black,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                NoRippleThemeProvides {
                                    TextSlider(
                                        value = viewModel.optionSortValue,
                                        onValueChange = viewModel::optionSortValue::set,
                                        text = {
                                            "${it.toInt()}"
                                        },
                                        range = (0f..100f)
                                    )
                                }

                                Text(
                                    text = "*值最大的记录会排在第一位",
                                    fontSize = 16.sp,
                                    color = Info
                                )
                            }
                        }
                    }
                }

                //加载对话框
                if (viewModel.showLoading) {
                    LoadingDialog()
                }
            }
        }
    }
}
