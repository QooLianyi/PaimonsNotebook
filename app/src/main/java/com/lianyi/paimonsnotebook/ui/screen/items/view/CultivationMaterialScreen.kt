package com.lianyi.paimonsnotebook.ui.screen.items.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.layout.column.TabBarColumnLayout
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyVerticalGrid
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.list_card.ItemGridListCard
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.material.materialTitle
import com.lianyi.paimonsnotebook.ui.screen.items.components.state.ItemScreenLoadingState
import com.lianyi.paimonsnotebook.ui.screen.items.data.ItemListCardData
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen.CultivationMaterialScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class CultivationMaterialScreen : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[CultivationMaterialScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PaimonsNotebookTheme(this) {

                ItemScreenLoadingState(loadingState = viewModel.loadingState) {

                    TabBarColumnLayout(
                        tabs = viewModel.tabs,
                        onTabBarSelect = viewModel::onChangePage,
                        topSlot = {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp, 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                Box {
                                    Text(
                                        text = viewModel.weekName,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 16.sp,
                                        modifier = Modifier
                                            .requiredWidthIn(60.dp,180.dp)
                                            .radius(2.dp)
                                            .clickable {
                                                viewModel.showDropMenu()
                                            }
                                            .padding(2.dp),
                                        textAlign = TextAlign.End
                                    )

                                    DropdownMenu(
                                        expanded = viewModel.showDropMenu,
                                        onDismissRequest = viewModel::dismissDropMenu
                                    ) {
                                        viewModel.dropMenuList.forEach { pair ->
                                            Text(
                                                text = pair.first,
                                                fontSize = 18.sp,
                                                modifier = Modifier
                                                    .width(100.dp)
                                                    .clickable {
                                                        viewModel.onSelectDropMenuItem(pair)
                                                    }
                                                    .padding(6.dp, 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    ) {
                        Crossfade(targetState = viewModel.currentPageIndex, label = "") {
                            ContentSpacerLazyVerticalGrid(
                                columns = GridCells.Adaptive(60.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(12.dp, 6.dp),
                                modifier = Modifier
                                    .fillMaxSize(),
                                statusBarPaddingEnabled = false
                            ) {
                                when (it) {
                                    1 -> {
                                        viewModel.weaponList.forEach { pair ->
                                            materialTitle(pair.first)

                                            items(pair.second, key = { it.id }) { weapon ->
                                                ItemGridListCard(
                                                    data = weapon,
                                                    itemListCardData = ItemListCardData(
                                                        iconUrl = weapon.iconUrl,
                                                        quality = weapon.rankLevel
                                                    ),
                                                    dataContent = weapon.name,
                                                    onClick = viewModel::onClickWeapon
                                                )
                                            }
                                        }
                                    }

                                    else -> {
                                        viewModel.avatarList.forEach { pair ->
                                            materialTitle(pair.first)

                                            items(pair.second, key = { it.id }) { weapon ->
                                                ItemGridListCard(
                                                    data = weapon,
                                                    itemListCardData = ItemListCardData(
                                                        iconUrl = weapon.iconUrl,
                                                        quality = weapon.starCount
                                                    ),
                                                    dataContent = weapon.name,
                                                    onClick = viewModel::onClickAvatar
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
    }
}