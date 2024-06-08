package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.data.MaterialBaseInfo
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.theme.White


@Composable
fun CultivateMaterialGroupHeader(
    materialBaseInfo: MaterialBaseInfo,
    imageSize:Dp = 46.dp,
    onShowMaterialInfoPopupDialog: (Material, InformationPopupPositionProvider) -> Unit
) {
    val material = materialBaseInfo.material

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        var offset = Offset.Zero
        var size = IntSize.Zero

        Box(
            Modifier
                .radius(4.dp)
                .size(imageSize)
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
                size = imageSize
            )
        }


        val content = materialBaseInfo.getShowContentAndColor()
        Text(
            text = content.first,
            fontSize = 12.sp,
            color = content.second,
            textAlign = TextAlign.Center
        )
    }
}