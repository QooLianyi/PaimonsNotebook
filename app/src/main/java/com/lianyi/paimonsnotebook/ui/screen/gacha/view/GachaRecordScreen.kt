package com.lianyi.paimonsnotebook.ui.screen.gacha.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.components.widget.TabBar
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.ui.screen.gacha.components.page.*
import com.lianyi.paimonsnotebook.ui.screen.gacha.util.UIGFHelper
import com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel.GachaRecordScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.*

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
                    ContentLoadingPlaceholder(modifier = Modifier.fillMaxSize())
                }

                LoadingState.Error -> {
                    ErrorPlaceholder {
                    }
                }

                else -> {
                    Column(modifier = Modifier.fillMaxSize()) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp, 6.dp, 12.dp, 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            TabBar(
                                modifier = Modifier
                                    .height(40.dp)
                                    .weight(1f),
                                tabs = viewModel.tabs,
                                onSelect = {
                                    viewModel.setSelectedPageIndex(it)
                                })

                            Icon(painter = painterResource(id = R.drawable.ic_navigation),
                                contentDescription = null,
                                tint = Black,
                                modifier = Modifier
                                    .radius(2.dp)
                                    .size(24.dp)
                                    .clickable {
                                        viewModel.goOptionScreen()
                                    })

                        }

                        Crossfade(targetState = viewModel.currentPageIndex, label = "") {
                            when (it) {
                                1 -> GachaItemsPage(viewModel.itemsList, UIGFHelper.ItemType.Avatar)
                                2 -> GachaItemsPage(viewModel.itemsList, UIGFHelper.ItemType.Weapon)
                                else -> {
                                    if(viewModel.gachaRecordOverview != null){
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
            }
        }
    }
}