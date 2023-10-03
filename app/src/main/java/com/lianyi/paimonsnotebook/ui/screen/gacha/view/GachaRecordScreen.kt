package com.lianyi.paimonsnotebook.ui.screen.gacha.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.layout.TabBarContent
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.page.GachaItemsPage
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.page.GachaRecordEmptyPage
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.page.GachaRecordOverviewPage
import com.lianyi.paimonsnotebook.ui.screen.gacha.util.UIGFHelper
import com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel.GachaRecordScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.Transparent

class GachaRecordScreen : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[GachaRecordScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme(this) {
                Content()
            }
        }
    }

    @Composable
    private fun Content() {

        Crossfade(
            targetState = viewModel.loadingState,
            modifier = Modifier
                .fillMaxSize()
                .background(BackGroundColor), label = ""
        ) { loadingState ->
            when (loadingState) {
                LoadingState.Empty -> {
                    GachaRecordEmptyPage {
                        viewModel.goOptionScreen()
                    }
                }

                LoadingState.Loading -> {
                    ContentLoadingPlaceholder()
                }

                LoadingState.Success -> {
                    TabBarContent(
                        onTabBarSelect = viewModel::setSelectedPageIndex,
                        tabs = viewModel.tabs,
                        indicatorColor = Transparent,
                        tabBarPadding = PaddingValues(horizontal = 12.dp),
                        contentPadding = PaddingValues(0.dp),
                        topSlot = {
                            Spacer(modifier = Modifier.weight(1f))

                            Icon(painter = painterResource(id = R.drawable.ic_navigation),
                                contentDescription = null,
                                tint = Black,
                                modifier = Modifier
                                    .align(Alignment.CenterVertically)
                                    .radius(2.dp)
                                    .size(24.dp)
                                    .clickable {
                                        viewModel.goOptionScreen()
                                    })

                            Spacer(modifier = Modifier.width(12.dp))
                        },
                        tabBarHeight = 35.dp
                    ) {
                        Crossfade(targetState = viewModel.currentPageIndex, label = "") {
                            when (it) {
                                1 -> GachaItemsPage(viewModel.itemsList, UIGFHelper.ItemType.Avatar)
                                2 -> GachaItemsPage(viewModel.itemsList, UIGFHelper.ItemType.Weapon)
                                else -> {
                                    if (viewModel.gachaRecordOverview != null) {
                                        GachaRecordOverviewPage(
                                            viewModel.gachaRecordOverview!!,
                                            viewModel.overviewItemMap
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                else -> {
                    ErrorPlaceholder {
                    }
                }
            }
        }
    }
}