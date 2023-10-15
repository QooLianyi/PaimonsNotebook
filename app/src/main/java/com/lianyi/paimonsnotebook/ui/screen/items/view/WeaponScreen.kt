package com.lianyi.paimonsnotebook.ui.screen.items.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.components.layout.blur_card.widget.ItemSlider
import com.lianyi.paimonsnotebook.common.components.text.RichText
import com.lianyi.paimonsnotebook.ui.screen.items.components.content.ItemScreenContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.material.ItemMaterialContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.property.ItemPropertyContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.layout.ItemInformationCardLayout
import com.lianyi.paimonsnotebook.ui.screen.items.components.state.ItemScreenLoadingState
import com.lianyi.paimonsnotebook.ui.screen.items.data.ItemListCardData
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen.WeaponScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme

class WeaponScreen : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[WeaponScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(intent)

        setContent {
            PaimonsNotebookTheme(this, lightStatusBar = false) {
                ItemScreenLoadingState(loadingState = viewModel.loadingState) {

                    ItemScreenContent(
                        backgroundImgUrl = viewModel.currentItem!!.gachaEquipImg,
                        itemImageContentScale = ContentScale.FillHeight,
                        listButtonText = "武器列表",
                        enabledItemShadow = true,
                        itemBackgroundResId = viewModel.currentItem!!.weaponGachaTypeBgResId,
                        baseInfoName = viewModel.currentItem!!.name,
                        baseInfoStarCount = viewModel.currentItem!!.rankLevel,
                        baseInfoIconUrl = viewModel.currentItem!!.weaponIconUrl,
                        tabs = viewModel.tabs,
                        itemFilterViewModel = viewModel.itemFilterViewModel,
                        onClickListButton = viewModel::toggleFilterContent,
                        getListItemDataContent = viewModel::getItemDataContent,
                        informationContentSlot = { weapon ->
                            InformationItem(
                                text = weapon.weaponTypeName,
                                iconUrl = weapon.weaponIconUrl,
                                paddingValues = PaddingValues(2.dp)
                            )
                        },
                        getItemListCardData = ItemListCardData::fromWeapon,
                        onClickListItemCard = viewModel::onClickItem,
                        cardContent = {
                            CardContent(it)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun CardContent(index: Int) {
        Crossfade(targetState = index, label = "") {
            when (it) {
                0 -> ItemPropertyContent(
                    iconUrl = viewModel.currentItem!!.iconUrl,
                    name = viewModel.currentItem!!.name,
                    compareIconUrl = viewModel.compareItem?.iconUrl ?: "",
                    propertyList = viewModel.propertyList,
                    compareItemPropertyList = viewModel.compareItemPropertyList,
                    showPromotedButton = false,
                    onClickCompareAvatar = viewModel::onClickCompareItem,
                    onLevelChange = viewModel::onChangeItemLevel,
                    onPromotedChange = viewModel::onPromotedChange,
                    informationSlot = {
                        InformationItem(
                            iconUrl = viewModel.currentItem!!.weaponIconUrl,
                            iconSize = 20.dp,
                            textSize = 14.sp,
                            text = viewModel.currentItem!!.weaponTypeName
                        )
                    }
                )

                1 -> {
                    if (viewModel.weaponAffixFormat != null) {
                        Column {
                            ItemInformationCardLayout(margin = 0.dp, contentSpacer = 8.dp) {
                                Text(
                                    viewModel.currentItem!!.affix?.Name ?: "",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )

                                RichText(text = viewModel.weaponAffixFormat!!.affixText)
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            ItemSlider(
                                value = viewModel.weaponAffixFormat!!.currentLevel,
                                onValueChange = viewModel.weaponAffixFormat!!::setLevel,
                                range = (1f..viewModel.weaponAffixFormat!!.maxLevel.toFloat()),
                            )
                        }
                    }
                }

                2 -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            ItemIconCard(
                                url = viewModel.currentItem!!.iconUrl,
                                star = viewModel.currentItem!!.rankLevel,
                                borderRadius = 4.dp
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = viewModel.currentItem!!.description,
                                fontSize = 12.sp
                            )
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            ItemIconCard(
                                url = viewModel.currentItem!!.awakenIconUrl,
                                star = viewModel.currentItem!!.rankLevel,
                                borderRadius = 4.dp
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Text(
                                text = "${viewModel.currentItem!!.name} 突破后",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                3 -> {
                    ItemMaterialContent(list = viewModel.materialList)
                }
            }
        }
    }
}