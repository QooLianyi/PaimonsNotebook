package com.lianyi.paimonsnotebook.ui.screen.home.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DrawerValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalDrawer
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.dialog.ConfirmDialog
import com.lianyi.paimonsnotebook.common.components.loading.LoadingAnimationPlaceholder
import com.lianyi.paimonsnotebook.common.components.spacer.NavigationBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.components.spacer.StatusBarPaddingSpacer
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.service.overlay.util.OverlayHelper
import com.lianyi.paimonsnotebook.ui.screen.account.view.AccountManagerScreen
import com.lianyi.paimonsnotebook.ui.screen.home.components.card.account.AccountInfoCard
import com.lianyi.paimonsnotebook.ui.screen.home.components.home.HomeContent
import com.lianyi.paimonsnotebook.ui.screen.home.components.menu.SideBarMenuList
import com.lianyi.paimonsnotebook.ui.screen.home.viewmodel.HomeScreenViewModel
import com.lianyi.paimonsnotebook.ui.screen.setting.util.configuration_enum.HomeScreenDisplayState
import com.lianyi.paimonsnotebook.ui.screen.setting.view.SettingsScreen
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black_90
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.White

class HomeScreen : BaseActivity() {
    private val viewModel by lazy {
        ViewModelProvider(this)[HomeScreenViewModel::class.java]
    }

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerPressedCallback()
        registerRequestPermissionsResult()
        viewModel.startActivity = registerStartActivityForResult()

        viewModel.init()

        setContent {
            PaimonsNotebookTheme {
                val coroutineScope = rememberCoroutineScope()
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                ModalDrawer(
                    modifier = Modifier.fillMaxSize(),
                    drawerState = drawerState,
                    drawerContent = {
                        Column(Modifier.fillMaxSize()) {

                            AccountInfoCard(viewModel.selectedUser) {
                                viewModel.functionNavigate(AccountManagerScreen::class.java)
                            }

                            SideBarMenuList(list = viewModel.modalItems) {
                                viewModel.navigateScreen(it)
                            }

                            Spacer(modifier = Modifier.weight(1f))

                            Image(painter = painterResource(id = R.drawable.ic_settings),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Black_90),
                                modifier = Modifier
                                    .padding(8.dp)
                                    .radius(2.dp)
                                    .size(36.dp)
                                    .align(Alignment.Start)
                                    .clickable {
                                        viewModel.functionNavigate(SettingsScreen::class.java)
                                    }
                                    .padding(4.dp)
                            )

                            NavigationBarPaddingSpacer()
                        }
                    },
                    drawerBackgroundColor = BackGroundColor
                ) {

                    val pullRefreshState = rememberPullRefreshState(
                        refreshing = viewModel.isRefreshing,
                        onRefresh = viewModel::refreshData
                    )

                    Box(
                        modifier = Modifier
                            .pullRefresh(pullRefreshState)
                            .fillMaxSize()
                            .background(White)
                    ) {

                        Column(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .zIndex(3f)
                        ) {
                            StatusBarPaddingSpacer()

                            Icon(
                                painter = painterResource(id = R.drawable.ic_navigation),
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(2.dp)
                                    .radius(3.dp)
                                    .size(32.dp)
                                    .clickable {
                                        viewModel.toggleModalDrawer(
                                            drawerState,
                                            coroutineScope
                                        )
                                    }
                                    .padding(4.dp)
                            )
                        }

                        Crossfade(
                            targetState = viewModel.configurationData.homeScreenDisplayState,
                            label = ""
                        ) {
                            when (it) {
                                HomeScreenDisplayState.Simple -> {
//                                    HomeContentSimple(
//                                        dailyNoteOverviewList = viewModel.dailyNoteList,
//                                        gachaRecordOverviewList = viewModel.gachaRecordOverviewList,
//                                        gachaEventList = viewModel.gachaEventList,
//                                        eventList = viewModel.eventList,
//                                        onNavigationClick = {
//
//                                        })
                                }

                                HomeScreenDisplayState.Community -> {
                                    HomeContent(
                                        bannerList = viewModel.bannerList,
                                        nearActivity = viewModel.nearActivity,
                                        noticeList = viewModel.noticeList,
                                        goPostDetail = viewModel::goPostDetail
                                    )
                                }

                                else -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        LoadingAnimationPlaceholder()
                                    }
                                }
                            }
                        }

                        PullRefreshIndicator(
                            refreshing = viewModel.isRefreshing,
                            state = pullRefreshState,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                }

                if (viewModel.showConfirm) {
                    ConfirmDialog(
                        title = getString(R.string.no_permission),
                        content = getString(R.string.content_request_overlay_permission),
                        onConfirm = viewModel::requestOverlayPermission,
                        onCancel = viewModel::removeOverlayPermissionFlag
                    )
                }
            }
        }
    }

    override fun onStartActivityForResult(result: ActivityResult) {
        viewModel.onActivityResult(result)
    }
}

