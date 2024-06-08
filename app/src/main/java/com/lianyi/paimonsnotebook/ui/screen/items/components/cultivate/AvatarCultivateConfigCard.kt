package com.lianyi.paimonsnotebook.ui.screen.items.components.cultivate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.data.cultivate.CultivateConfigData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun AvatarCultivateConfigCard(
    avatarData: AvatarData,
    list: List<CultivateConfigData>
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {

        CultivateConfigHeaderSlot(
            iconUrl = avatarData.iconUrl,
            name = avatarData.name,
            starCount = avatarData.starCount,
            tagSlot = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InformationItem(
                        iconResId = avatarData.fetterInfo.elementIconResId,
                        text = avatarData.fetterInfo.VisionBefore,
                        textColor = White,
                        backgroundColor = avatarData.fetterInfo.elementColor,
                        textSize = 12.sp,
                        paddingValues = PaddingValues(6.dp, 2.5.dp)
                    )

                    InformationItem(
                        iconUrl = avatarData.weaponIconUrl,
                        text = avatarData.weaponTypeName,
                        textColor = White,
                        backgroundColor = avatarData.fetterInfo.elementColor,
                        textSize = 12.sp,
                        paddingValues = PaddingValues(6.dp, 2.5.dp)
                    )
                }
            },
            baseInfoSlot = {
                NetworkImageForMetadata(
                    url = avatarData.fetterInfo.associationIconUrl,
                    modifier = Modifier
                        .radius(4.dp)
                        .size(60.dp),
                    contentScale = ContentScale.FillWidth,
                    alignment = BiasAlignment(0f, -0.4f),
                    tint = Black
                )
            }
        )

        list.forEach { config ->
            CultivateConfigListItem(config)
        }
    }
}