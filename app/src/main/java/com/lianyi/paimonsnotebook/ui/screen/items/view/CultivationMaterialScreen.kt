package com.lianyi.paimonsnotebook.ui.screen.items.view

import android.os.Bundle
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.list_card.ItemGridListCard
import com.lianyi.paimonsnotebook.ui.screen.items.components.state.ItemScreenLoadingState
import com.lianyi.paimonsnotebook.ui.screen.items.data.ItemListCardData
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen.CultivationMaterialScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.BackGroundColor
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
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(BackGroundColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box {
                                Text(
                                    text = viewModel.weekName,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    modifier = Modifier
                                        .width(60.dp)
                                        .radius(2.dp)
                                        .clickable {
                                            viewModel.showDropMenu()
                                        }
                                        .padding(2.dp)
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
                                                .padding(6.dp,3.dp)
                                        )
                                    }
                                }
                            }
                        }

                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(60.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(8.dp),
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            if (viewModel.avatarList.isNotEmpty()) {
                                item(span = {
                                    GridItemSpan(this.maxLineSpan)
                                }) {
                                    Text(
                                        text = "天赋培养",
                                        fontSize = 16.sp,
                                    )
                                }
                                items(viewModel.avatarList, key = { it.id }) { avatar ->
                                    ItemGridListCard(
                                        data = avatar,
                                        itemListCardData = ItemListCardData(
                                            iconUrl = avatar.iconUrl,
                                            quality = avatar.starCount
                                        ),
                                        dataContent = avatar.name,
                                        onClick = viewModel::onClickAvatar
                                    )
                                }
                            }

                            if (viewModel.weaponList.isNotEmpty()) {
                                item(span = {
                                    GridItemSpan(this.maxLineSpan)
                                }) {
                                    Text(
                                        text = "武器突破",
                                        fontSize = 16.sp,
                                    )
                                }
                                items(viewModel.weaponList, key = { it.id }) { weapon ->
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
                    }
                }
            }
        }
    }
}