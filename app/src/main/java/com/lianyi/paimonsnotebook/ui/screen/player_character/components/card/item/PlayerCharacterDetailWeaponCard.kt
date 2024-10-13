package com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.core.ui.components.text.RichText
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.FightProperty
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color
import com.lianyi.paimonsnotebook.ui.theme.White
import com.lianyi.paimonsnotebook.ui.theme.White_30
import com.lianyi.paimonsnotebook.ui.theme.White_40
import com.lianyi.paimonsnotebook.ui.theme.White_70

@Composable
fun PlayerCharacterDetailWeaponCard(
    weaponData: WeaponData,
    level: Int,
    affixLevel: Int,
    weaponFightPropertyFormatList: List<FightPropertyFormat>,
    backgroundColor: Color = White_70,
    clickable: Boolean = false
) {
    var showDesc by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .radius(4.dp)
            .background(backgroundColor)
            .then(
                if (clickable) {
                    Modifier.clickable {
                        showDesc = !showDesc
                    }
                } else Modifier
            )
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            NetworkImageForMetadata(
                url = weaponData.iconUrl,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    PrimaryText(
                        text = "${weaponData.name}  Lv.${level}",
                        textSize = 13.sp
                    )
                    InformationItem(
                        text = "R${affixLevel}",
                        backgroundColor = GachaStar5Color,
                        textSize = 10.sp,
                        paddingValues = PaddingValues(
                            3.dp,
                            2.dp
                        ),
                        textColor = White
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(
                            id = FightProperty.getIconResourceByProperty(
                                weaponFightPropertyFormatList.first().property
                            )
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(16.dp)
                            .background(White_40)
                            .padding(2.dp)
                    )

                    PrimaryText(
                        text = weaponFightPropertyFormatList.first().formatValue,
                        textSize = 12.sp
                    )

                    Icon(
                        painter = painterResource(
                            id = FightProperty.getIconResourceByProperty(
                                weaponFightPropertyFormatList.last().property
                            )
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(16.dp)
                            .background(White_30)
                            .padding(2.dp)
                    )

                    PrimaryText(
                        text = weaponFightPropertyFormatList.last().formatValue,
                        textSize = 12.sp
                    )
                }
            }
        }

        AnimatedVisibility(visible = showDesc) {
            val description = if (weaponData.affix != null) {
                val targetAffixLevel = affixLevel - 1

                if (targetAffixLevel >= weaponData.affix.Descriptions.size || targetAffixLevel < 0) {
                    weaponData.description
                } else {
                    weaponData.affix.Descriptions[targetAffixLevel].Description
                }
            } else {
                weaponData.description
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                if (weaponData.affix != null) {
                    PrimaryText(text = weaponData.affix.Name, textSize = 14.sp)
                }

                RichText(text = description, fontSize = 12.sp)
            }
        }
    }
}