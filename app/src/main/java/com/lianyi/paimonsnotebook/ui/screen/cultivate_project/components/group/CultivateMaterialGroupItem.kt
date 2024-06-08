package com.lianyi.paimonsnotebook.ui.screen.cultivate_project.components.group

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.ui.screen.cultivate_project.data.EntityBaseInfo
import com.lianyi.paimonsnotebook.ui.screen.items.components.item.icon.ItemIconCard
import com.lianyi.paimonsnotebook.ui.theme.White


@Composable
fun CultivateMaterialGroupItem(
    entityBaseInfo: EntityBaseInfo,
    onShowEntityInfoPopupDialog: (Int, InformationPopupPositionProvider) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        var offset = Offset.Zero
        var size = IntSize.Zero

        Box(
            modifier = Modifier
                .radius(4.dp)
                .size(46.dp)
                .background(White)
                .onGloballyPositioned {
                    offset = it.positionInRoot()
                    size = it.size
                }
                .clickable {
                    onShowEntityInfoPopupDialog.invoke(
                        entityBaseInfo.id, InformationPopupPositionProvider(
                            contentOffset = offset,
                            itemSize = size,
                            itemSpace = 8.dp
                        )
                    )
                }
        ) {
            ItemIconCard(
                url = entityBaseInfo.iconUrl,
                star = entityBaseInfo.star,
                borderRadius = 4.dp,
                size = 46.dp
            )
        }
    }
}