package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.material

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItemMaterials
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItems
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.items.CultivateItemPropertyPromoteInfo

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CultivateVerticalMaterialList(
    list: List<CultivateItemMaterials>,
    showLackNum: Boolean,
    onShowMaterialInfoPopupDialog: (Material, InformationPopupPositionProvider) -> Unit,
    getMaterialInfo: (Int) -> Material,
    onClickMaterialItem: (CultivateItemMaterials) -> Unit
) {
    val rows = list.size % 2 + list.size / 2

    val totalItemHeight = 30.dp * rows
    val spacerHeight = 6.dp * (rows - 1)
    val gridListHeight = totalItemHeight + spacerHeight

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .height(gridListHeight),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        list.forEach { cultivateItemMaterials ->
            item(key = "${cultivateItemMaterials.projectId}_${cultivateItemMaterials.cultivateItemId}_${cultivateItemMaterials.itemId}") {
                Box(modifier = Modifier.animateItemPlacement()) {
                    VerticalCultivateMaterialItem(
                        cultivateItemMaterials = cultivateItemMaterials,
                        getMaterialInfo = getMaterialInfo,
                        showLackNum = showLackNum,
                        onShowMaterialInfoPopupDialog = onShowMaterialInfoPopupDialog,
                        onClickMaterialItem = onClickMaterialItem
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CultivateVerticalSkillMaterialList(
    cultivateItems: List<CultivateItems>,
    showLackNum: Boolean,
    skillIdMap: Map<Int, AvatarData.Skill>,
    onShowMaterialInfoPopupDialog: (Material, InformationPopupPositionProvider) -> Unit,
    getMaterialsByCultivateItemId: (Int) -> List<CultivateItemMaterials>,
    getMaterialInfo: (Int) -> Material,
) {
    FlowRow(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        maxItemsInEachRow = 2
    ) {

        cultivateItems.forEach { cultivateItem ->
            //如果此项为空代表显示的不是技能数据
            val skill = skillIdMap[cultivateItem.itemId] ?: return@forEach
            val materials = getMaterialsByCultivateItemId.invoke(skill.GroupId)

            CultivateItemPropertyPromoteInfo(
                iconUrl = skill.iconUrl,
                name = skill.Name,
                fromLevel = cultivateItem.fromLevel,
                toLevel = cultivateItem.toLevel,
                alignBothSide = true
            )

            materials.forEach { cultivateItemMaterials ->
                Row(modifier = Modifier.weight(1f)) {
                    VerticalCultivateMaterialItem(
                        cultivateItemMaterials = cultivateItemMaterials,
                        getMaterialInfo = getMaterialInfo,
                        showLackNum = showLackNum,
                        onShowMaterialInfoPopupDialog = onShowMaterialInfoPopupDialog,
                    )
                }
            }

            if (materials.size % 2 != 0) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}