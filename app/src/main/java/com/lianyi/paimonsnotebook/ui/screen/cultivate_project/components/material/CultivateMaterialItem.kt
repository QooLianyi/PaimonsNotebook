package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.material

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.common.database.cultivate.entity.CultivateItemMaterials
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun VerticalCultivateMaterialItem(
    cultivateItemMaterials: CultivateItemMaterials,
    getMaterialInfo: (Int) -> Material,
    showLackNum: Boolean = true,
    onShowMaterialInfoPopupDialog: (Material, InformationPopupPositionProvider) -> Unit,
    onClickMaterialItem: (CultivateItemMaterials) -> Unit = {}
) {
    val material = getMaterialInfo.invoke(cultivateItemMaterials.itemId)

    Row(modifier = Modifier
        .radius(2.dp)
        .clickable {
            onClickMaterialItem.invoke(cultivateItemMaterials)
        }
        .padding(end = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        var offset = Offset.Zero
        var size = IntSize.Zero


        Box(
            modifier = Modifier
                .radius(4.dp)
                .size(30.dp)
                .background(White)
                .onGloballyPositioned {
                    offset = it.positionInRoot()
                    size = it.size
                }
                .clickable {
                    onShowMaterialInfoPopupDialog.invoke(
                        material, InformationPopupPositionProvider(
                            contentOffset = offset,
                            itemSize = size,
                            itemSpace = 8.dp
                        )
                    )
                }
        ) {
            ItemIconCard(
                url = material.iconUrl,
                star = material.RankLevel,
                borderRadius = 4.dp,
                size = 30.dp
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = material.Name,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Start
        )

        val content = cultivateItemMaterials.getShowContentAndColor(showLackNum)

        Text(
            text = content.first,
            fontSize = 12.sp,
            color = content.second
        )
    }
}
