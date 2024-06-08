package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.material

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateEntityType
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateEntity
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItemMaterials
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItems
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.util.CultivateMaterialListDisplayState
import com.lianyi.paimonsnotebook.ui.theme.Black
import kotlinx.coroutines.delay

@Composable
fun CultivateMaterialGroup(
    entity: CultivateEntity,
    cultivateItemsMap: Map<Int, CultivateItems>,
    getMaterialsByCultivateItemId: (Int) -> List<CultivateItemMaterials>,
    cultivateItems: List<CultivateItems>? = null,
    avatarData: AvatarData? = null,
    weaponData: WeaponData? = null,
    getMaterialInfo: (Int) -> Material,
    onEmitMaterialItemUpdateQueue: (CultivateItems, List<CultivateItemMaterials>) -> Unit,
    onShowMaterialInfoPopupDialog: (Material, InformationPopupPositionProvider) -> Unit
) {
    //当没有传入所需数据时,直接返回
    if (entity.type == CultivateEntityType.Avatar && avatarData == null || entity.type == CultivateEntityType.Avatar && cultivateItems == null) return
    if (entity.type == CultivateEntityType.Weapon && weaponData == null) return
    if (cultivateItemsMap.isEmpty()) return

    var materialListDisplayState by remember {
        mutableStateOf(CultivateMaterialListDisplayState.Overall)
    }

    var showLackNum by remember {
        mutableStateOf(true)
    }

    val groupName by remember {
        derivedStateOf {
            when (materialListDisplayState) {
                CultivateMaterialListDisplayState.Overall -> "缺少的材料"
                CultivateMaterialListDisplayState.All -> "养成消耗总和"
                CultivateMaterialListDisplayState.Weapon -> "武器消耗"
                CultivateMaterialListDisplayState.Avatar -> "角色消耗"
                CultivateMaterialListDisplayState.Skill -> "天赋消耗"
                else -> ""
            }
        }
    }


    var currentCultivateItemsId = remember {
        -entity.itemId
    }

    val clickMaterialUpdateQueue = remember {
        mutableStateListOf<CultivateItemMaterials>()
    }

    val list = remember(entity.projectId, entity.itemId, materialListDisplayState) {
        currentCultivateItemsId = when (materialListDisplayState) {
            CultivateMaterialListDisplayState.Overall,
            CultivateMaterialListDisplayState.All -> -entity.itemId

            CultivateMaterialListDisplayState.Weapon -> cultivateItemsMap[weaponData!!.id]?.itemId
                ?: 0

            CultivateMaterialListDisplayState.Avatar -> cultivateItemsMap[avatarData!!.id]?.itemId
                ?: 0

            else -> 0
        }

        mutableStateListOf<CultivateItemMaterials>().apply {
            this.clear()
            this += getMaterialsByCultivateItemId.invoke(currentCultivateItemsId)
        }
    }

    //一段时间内的操作批量提交更新
    LaunchedEffect(clickMaterialUpdateQueue.size) {
        if (clickMaterialUpdateQueue.isEmpty()) return@LaunchedEffect

        //点击后的延迟提交时间,如果在倒计时期间重复点击当前协程会被重启
        delay(800)

        val blockCultivateItems = cultivateItemsMap[currentCultivateItemsId]
            ?: return@LaunchedEffect

        onEmitMaterialItemUpdateQueue.invoke(
            blockCultivateItems,
            clickMaterialUpdateQueue.toList()
        )

        clickMaterialUpdateQueue.clear()
    }


    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PrimaryText(text = groupName, fontSize = 14.sp)

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(
                    id = when (materialListDisplayState) {
                        CultivateMaterialListDisplayState.Avatar -> R.drawable.ic_genshin_game_character
                        CultivateMaterialListDisplayState.Skill -> R.drawable.ic_star_outline
                        CultivateMaterialListDisplayState.Weapon -> R.drawable.ic_genshin_game_equip
                        CultivateMaterialListDisplayState.Overall -> R.drawable.ic_list_square
                        else -> R.drawable.ic_circle_empty
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .radius(2.dp)
                    .size(22.dp)
                    .clickable {
                        materialListDisplayState = if (entity.type == CultivateEntityType.Avatar) {
                            when (materialListDisplayState) {
                                CultivateMaterialListDisplayState.Overall -> CultivateMaterialListDisplayState.All
                                CultivateMaterialListDisplayState.All -> CultivateMaterialListDisplayState.Avatar
                                CultivateMaterialListDisplayState.Avatar -> CultivateMaterialListDisplayState.Skill
                                CultivateMaterialListDisplayState.Skill -> CultivateMaterialListDisplayState.Overall
                                else -> CultivateMaterialListDisplayState.All
                            }
                        } else {
                            when (materialListDisplayState) {
                                CultivateMaterialListDisplayState.Overall -> CultivateMaterialListDisplayState.All
                                CultivateMaterialListDisplayState.All -> CultivateMaterialListDisplayState.Overall
                                else -> CultivateMaterialListDisplayState.Overall
                            }
                        }

                        //如果显示的是总览视图就显示缺少的个数
                        showLackNum =
                            materialListDisplayState == CultivateMaterialListDisplayState.Overall
                    },
                tint = Black
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        AnimatedVisibility(visible = materialListDisplayState != CultivateMaterialListDisplayState.Skill) {
            CultivateVerticalMaterialList(
                list = list,
                showLackNum = showLackNum,
                onShowMaterialInfoPopupDialog = onShowMaterialInfoPopupDialog,
                getMaterialInfo = getMaterialInfo,
                onClickMaterialItem = { cultivateItemMaterials ->
                    val index = clickMaterialUpdateQueue.indexOf(cultivateItemMaterials)

                    if (index == -1) {
                        clickMaterialUpdateQueue += cultivateItemMaterials
                    } else {
                        clickMaterialUpdateQueue -= cultivateItemMaterials
                    }
                    cultivateItemMaterials.switchTempStatus()

                    list.sortWith(compareBy({ it.tempStatus }, { it.itemId }))
                }
            )
        }

        AnimatedVisibility(visible = materialListDisplayState == CultivateMaterialListDisplayState.Skill) {
            CultivateVerticalSkillMaterialList(
                cultivateItems = cultivateItems!!,
                showLackNum = showLackNum,
                skillIdMap = avatarData!!.skillDepot.skillIdMap,
                onShowMaterialInfoPopupDialog = onShowMaterialInfoPopupDialog,
                getMaterialInfo = getMaterialInfo,
                getMaterialsByCultivateItemId = getMaterialsByCultivateItemId,
            )
        }
    }

}