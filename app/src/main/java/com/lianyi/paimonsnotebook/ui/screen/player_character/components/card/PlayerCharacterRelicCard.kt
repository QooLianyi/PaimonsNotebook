package com.lianyi.paimonsnotebook.ui.screen.player_character.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.extension.list.split
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterDetailData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.RelicIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.FightProperty
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ReliquaryType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquaryData
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color2
import com.lianyi.paimonsnotebook.ui.theme.White_40
import com.lianyi.paimonsnotebook.ui.theme.White_70

@Composable
fun PlayerCharacterRelicCard(
    relicList: List<CharacterDetailData.Relic>,
    getRelicById: (Int) -> ReliquaryData?,
    recommendRelicProperty: CharacterDetailData.RecommendRelicProperty,
    onClickRelicIcon: (ReliquaryData, IntSize, Offset) -> Unit
) {
    //如果为空直接跳过渲染
    if (relicList.isEmpty()) return

    val recommendPropertySetMap =
        remember(relicList.size, relicList.first().hashCode()) {
            mapOf(
                ReliquaryType.EQUIP_SHOES to recommendRelicProperty.recommend_properties.sand_main_property_list.toSet(),
                ReliquaryType.EQUIP_RING to recommendRelicProperty.recommend_properties.goblet_main_property_list.toSet(),
                ReliquaryType.EQUIP_DRESS to recommendRelicProperty.recommend_properties.circlet_main_property_list.toSet(),
                ReliquaryType.EQUIP_NONE to recommendRelicProperty.recommend_properties.sub_property_list.toSet()
            )
        }

    Column(
        modifier = Modifier
            .radius(6.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        relicList.split(2).forEach { relics ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                relics.forEach { relic ->
                    val reliquaryData = getRelicById.invoke(relic.id) ?: return@forEach

                    Column(
                        modifier = Modifier
                            .radius(6.dp)
                            .weight(1f)
                            .background(White_40)
                            .padding(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row {
                            var size = IntSize.Zero
                            var offset = Offset.Zero
                            Box(
                                modifier = Modifier
                                    .radius(4.dp)
                                    .clickable {
                                        onClickRelicIcon.invoke(reliquaryData, size, offset)
                                    },
                                contentAlignment = Alignment.BottomEnd
                            ) {
                                NetworkImageForMetadata(
                                    url = RelicIconConverter.iconNameToUrl(reliquaryData.Icon),
                                    modifier = Modifier
                                        .radius(4.dp)
                                        .size(42.dp)
                                        .background(White_40)
                                        .padding(2.dp)
                                        .onGloballyPositioned {
                                            size = it.size
                                            offset = it.positionInRoot()
                                        }
                                )

                                PrimaryText(
                                    text = "+${relic.level}",
                                    color = GachaStar5Color,
                                    textSize = 8.sp,
                                    modifier = Modifier.padding(2.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                PrimaryText(
                                    text = reliquaryData.Name,
                                    textSize = 12.sp,
                                    maxLines = 1
                                )

                                val color =
                                    if (recommendPropertySetMap[relic.pos]?.contains(relic.main_property.property_type) == true) GachaStar5Color2 else Black

                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = FightProperty.getIconResourceByProperty(relic.main_property.property_type)
                                        ),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(16.dp)
                                            .background(White_40)
                                            .padding(2.dp),
                                        tint = color
                                    )

                                    PrimaryText(
                                        text = relic.main_property.value,
                                        textSize = 12.sp,
                                        color = color
                                    )
                                }
                            }
                        }

                        relic.sub_property_list.forEach { subProperty ->
                            val color =
                                if (recommendPropertySetMap[ReliquaryType.EQUIP_NONE]?.contains(
                                        subProperty.property_type
                                    ) == true
                                ) GachaStar5Color2 else Black

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {

                                Box(modifier = Modifier.size(24.dp, 16.dp)) {
                                    Icon(
                                        painter = painterResource(
                                            id = FightProperty.getIconResourceByProperty(
                                                subProperty.property_type
                                            )
                                        ),
                                        contentDescription = "",
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(16.dp)
                                            .background(White_40)
                                            .padding(2.dp),
                                        tint = color
                                    )

                                    if (subProperty.times > 0) {
                                        Box(
                                            modifier = Modifier
                                                .clip(CircleShape)
                                                .size(10.dp)
                                                .background(White_70)
                                                .padding(1.dp)
                                                .align(Alignment.BottomEnd),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "${subProperty.times}",
                                                fontSize = 8.sp,
                                                color = color,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }

                                PrimaryText(
                                    text = FightProperty.getNameByProperty(subProperty.property_type),
                                    textSize = 12.sp,
                                    color = color,
                                    maxLines = 1,
                                    modifier = Modifier.weight(1f)
                                )

                                PrimaryText(
                                    text = subProperty.value, color = color,
                                    textSize = 12.sp
                                )
                            }
                        }
                    }

                    if (relics.size < 2) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}