package com.lianyi.paimonsnotebook.ui.screen.player_character.view

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.popup.IconTitleInformationPopupWindow
import com.lianyi.paimonsnotebook.common.core.base.BaseActivity
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ElementType
import com.lianyi.paimonsnotebook.ui.screen.items.components.content.ItemScreenContent
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.components.state.ItemScreenLoadingState
import com.lianyi.paimonsnotebook.ui.screen.items.data.ItemListCardData
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.PlayerCharacterListCard
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.PlayerCharacterPropertyCard
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.PlayerCharacterRelicCard
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item.PlayerCharacterDetailSkillCard
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item.PlayerCharacterDetailTalentCard
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item.PlayerCharacterDetailWeaponCard
import com.lianyi.paimonsnotebook.ui.screen.player_character.viewmodel.PlayerCharacterDetailScreenViewModel
import com.lianyi.paimonsnotebook.ui.theme.FetterColor
import com.lianyi.paimonsnotebook.ui.theme.PaimonsNotebookTheme
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_40

class PlayerCharacterDetailScreen : BaseActivity() {

    companion object {
        //当前用户与uid
        const val PARAM_USER_AND_UID_JSON = "user_and_uid_json"

        //玩家角色的列表集合
        const val PARAM_CHARACTER_LIST_JSON = "character_list_json"

        const val PARAM_SELECTED_CHARACTER_ID = "selected_character_id"
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[PlayerCharacterDetailScreenViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init(intent) {
            finish()
        }

        setContent {
            PaimonsNotebookTheme(this) {

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
                        showAddButton = false,
                        baseInfoSlot = {
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Spacer(modifier = Modifier.width(1.dp))

                                InformationItem(
                                    text = "Lv.${viewModel.currentCharacterDetail?.base?.level}",
                                    backgroundColor = White_40,
                                    textSize = 14.sp,
                                    paddingValues = PaddingValues(
                                        6.dp,
                                        2.dp
                                    )
                                )

                                InformationItem(
                                    text = "${viewModel.currentCharacterDetail?.base?.fetter}",
                                    iconResId = R.drawable.icon_fetter,
                                    backgroundColor = White_40,
                                    textSize = 14.sp,
                                    paddingValues = PaddingValues(
                                        6.dp,
                                        2.dp
                                    ),
                                    tint = FetterColor,
                                    textColor = FetterColor
                                )
                            }
                        },
                        getItemListCardData = ItemListCardData::fromAvatar,
                        onClickListItemCard = viewModel::onClickItem,
                        onClickAddButton = viewModel::addCurrentItemToCultivateProject,
                        itemAddedCurrentCultivateProject = viewModel.itemAddedToCurrentCultivateProject,
                        verticalListCardContent = { avatarData, _ ->
                            val characterData = viewModel.getCharacterListDataById(avatarData.id)
                                ?: return@ItemScreenContent

                            PlayerCharacterListCard(
                                characterData = characterData,
                                getAvatarDataById = viewModel::getAvatarDataById,
                                getWeaponDataById = viewModel::getWeaponDataById,
                                getWeaponFightPropertyFormatList = viewModel::getWeaponFightPropertyFormatList,
                                onClick = {
                                    viewModel.toggleFilterContent()
                                    viewModel.onClickItem(avatarData)
                                }
                            )
                        },
                        cardContent = {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
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

                            val character by remember(viewModel.currentCharacterDetail?.base?.id) {
                                mutableStateOf(viewModel.currentCharacterDetail)
                            }
                            val avatarData = viewModel.getAvatarDataById(character?.base?.id ?: -1)

                            if (character != null && avatarData != null) {
                                PlayerCharacterDetailSkillCard(
                                    skillDepot = avatarData.skillDepot,
                                    elementTypeName = character!!.base.element,
                                    skillLevelMap = character!!.skills.associate {
                                        it.skill_id to it.level
                                    },
                                    backgroundColor = White_40,
                                )

                                PlayerCharacterDetailTalentCard(
                                    talents = avatarData.skillDepot.Talents,
                                    elementTypeName = character!!.base.element,
                                    activateCount = character!!.base.actived_constellation_num,
                                    backgroundColor = White_40,
                                    clickable = true
                                )
                            }

                            PlayerCharacterPropertyCard(
                                propertyList = viewModel.currentCharacterDetail?.selected_properties
                                    ?: listOf(),
                                extraPropertyList = viewModel.currentCharacterDetail?.let {
                                    it.base_properties + it.extra_properties + it.element_properties
                                } ?: listOf()
                            )

                            val weapon = viewModel.currentCharacterDetail?.weapon
                            val weaponData = viewModel.getWeaponDataById(weapon?.id ?: -1)

                            if (weaponData != null && weapon != null) {
                                val list = remember(weapon.id, weapon.level) {
                                    viewModel.getWeaponFightPropertyFormatList(
                                        weaponData = weaponData,
                                        level = weapon.level,
                                        promoted = true
                                    )
                                }

                                PlayerCharacterDetailWeaponCard(
                                    weaponData = weaponData,
                                    level = weapon.level,
                                    affixLevel = weapon.affix_level,
                                    weaponFightPropertyFormatList = list,
                                    backgroundColor = White_40,
                                    clickable = true
                                )
                            }

                            val relics = viewModel.currentCharacterDetail?.relics
                            val recommendRelicProperty =
                                viewModel.currentCharacterDetail?.recommend_relic_property

                            if (!relics.isNullOrEmpty() && recommendRelicProperty != null) {
                                PlayerCharacterRelicCard(
                                    relicList = relics,
                                    getRelicById = viewModel::getRelicById,
                                    recommendRelicProperty = recommendRelicProperty,
                                    onClickRelicIcon = viewModel::onClickRelicIcon
                                )
                            }
                        }
                    )

                    if(viewModel.showReliquarySetInfoPopupWindow){
                        IconTitleInformationPopupWindow(
                            data = viewModel.reliquarySetInfoDataSet,
                            popupProvider = viewModel.reliquarySetInfoPopupWindowProvider,
                            onDismissRequest = viewModel::onPopupWindowDismissRequest
                        )
                    }
                }
            }
        }
    }
}