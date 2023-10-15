package com.lianyi.paimonsnotebook.ui.screen.items.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.AssociationIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.AssociationType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ElementType
import com.lianyi.paimonsnotebook.ui.screen.items.components.content.ItemScreenContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.avatar.content.information.AvatarInformationContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.avatar.content.skill.AvatarSkillContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.material.ItemMaterialContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.property.ItemPropertyContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.state.ItemScreenLoadingState
import com.lianyi.paimonsnotebook.ui.screen.items.data.ItemListCardData
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen.AvatarScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.White

class AvatarScreen : ComponentActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this)[AvatarScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(intent)

        setContent {
            PaimonsNotebookTheme(this, lightStatusBar = false) {
                ItemScreenLoadingState(loadingState = viewModel.loadingState) {

                    ItemScreenContent(
                        backgroundImgUrl = viewModel.currentItem!!.gachaAvatarImg,
                        listButtonText = "角色列表",
                        baseInfoName = viewModel.currentItem!!.name,
                        baseInfoStarCount = viewModel.currentItem!!.starCount,
                        baseInfoIconUrl = viewModel.currentItem!!.fetterInfo.associationIconUrl,
                        tabs = viewModel.tabs,
                        itemFilterViewModel = viewModel.itemFilterViewModel,
                        onClickListButton = viewModel::toggleFilterContent,
                        getListItemDataContent = viewModel::getItemDataContent,
                        informationContentSlot = { avatar ->
                            InformationItem(
                                text = AssociationType.getAssociationNameByType(avatar.fetterInfo.Association),
                                iconUrl = AssociationIconConverter.avatarAssociationToUrl(avatar.fetterInfo.Association),
                                paddingValues = PaddingValues(2.dp)
                            )

                            InformationItem(
                                backgroundColor = ElementType.getElementColorByName(avatar.fetterInfo.VisionBefore),
                                iconResId = ElementType.getElementResourceIdByName(avatar.fetterInfo.VisionBefore),
                                textColor = White,
                                paddingValues = PaddingValues(2.dp)
                            )
                        },
                        getItemListCardData = ItemListCardData::fromAvatar,
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
                    onClickCompareAvatar = viewModel::onClickCompareItem,
                    onLevelChange = viewModel::onChangeItemLevel,
                    onPromotedChange = viewModel::onPromotedChange,
                    informationSlot = {
                        InformationItem(
                            iconResId = ElementType.getElementResourceIdByName(viewModel.currentItem!!.fetterInfo.VisionBefore),
                            textColor = White,
                            iconSize = 20.dp,
                            textSize = 14.sp,
                            text = viewModel.currentItem!!.fetterInfo.VisionBefore,
                            backgroundColor = ElementType.getElementColorByName(viewModel.currentItem!!.fetterInfo.VisionBefore),
                        )

                        InformationItem(
                            iconUrl = viewModel.currentItem!!.weaponIconUrl,
                            textColor = White,
                            iconSize = 20.dp,
                            textSize = 14.sp,
                            text = viewModel.currentItem!!.weaponTypeName,
                            backgroundColor = ElementType.getElementColorByName(viewModel.currentItem!!.fetterInfo.VisionBefore),
                        )
                    }
                )

                1 -> AvatarSkillContent(
                    skillList = viewModel.skillList,
                    iconBackgroundColor = viewModel.currentItem!!.fetterInfo.elementColor
                )

                2 -> AvatarSkillContent(
                    skillList = viewModel.talentList,
                    iconBackgroundColor = viewModel.currentItem!!.fetterInfo.elementColor,
                    enabledIconBorder = true
                )

                3 -> AvatarInformationContent(avatar = viewModel.currentItem!!)

                4 -> ItemMaterialContent(list = viewModel.materialList)
            }
        }
    }
}