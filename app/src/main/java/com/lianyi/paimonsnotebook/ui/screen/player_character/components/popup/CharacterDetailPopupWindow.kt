package com.lianyi.paimonsnotebook.ui.screen.player_character.components.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterDetailData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.FightProperty
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.StarGroup
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun CharacterDetailPopupWindow(
    characterDetailData: CharacterDetailData,
    getAvatarDataById: (Int) -> AvatarData,
    getWeaponDataById: (Int) -> WeaponData,
    getPropertyDataById: (Int) -> CharacterDetailData.PropertyMapData?
) {
    val detailItem = remember(Unit) {
        characterDetailData.list.first()
    }

    val avatarData = remember(detailItem.base.id) {
        getAvatarDataById.invoke(detailItem.base.id)
    }

    val weaponData = remember(detailItem.base.id) {
        getWeaponDataById.invoke(detailItem.weapon.id)
    }


    Row(modifier = Modifier.height(IntrinsicSize.Min)) {

        NetworkImageForMetadata(
            url = avatarData.gachaAvatarIcon,
            modifier = Modifier
                .radius(4.dp)
                .width(70.dp)
                .fillMaxHeight(),
            contentScale = ContentScale.FillWidth,
            alignment = BiasAlignment(0f, -0.4f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(
                8.dp
            )
        ) {

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp
                )
            ) {
                Text(
                    text = detailItem.base.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )

                InformationItem(
                    text = "Lv.${detailItem.base.level}",
                    backgroundColor = White,
                    textSize = 13.sp,
                    paddingValues = PaddingValues(
                        6.dp,
                        2.5.dp
                    )
                )

                InformationItem(
                    text = "${detailItem.base.fetter}",
                    iconResId = R.drawable.icon_fetter,
                    backgroundColor = White,
                    textSize = 13.sp,
                    paddingValues = PaddingValues(
                        6.dp,
                        2.5.dp
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp
                )
            ) {

                InformationItem(
                    iconResId = avatarData.fetterInfo.elementIconResId,
                    text = avatarData.fetterInfo.VisionBefore,
                    textColor = White,
                    backgroundColor = avatarData.fetterInfo.elementColor,
                    textSize = 12.sp,
                    paddingValues = PaddingValues(
                        6.dp,
                        2.5.dp
                    )
                )

                StarGroup(
                    starCount = avatarData.starCount,
                    starSize = 14.dp,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(White)
                        .padding(6.dp, 4.dp)
                )
            }


            Row {

                NetworkImageForMetadata(url = weaponData.iconUrl)

                Column {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        PrimaryText(
                            text = "${weaponData.name}  ${detailItem.weapon.level}",
                            fontSize = 12.sp
                        )
                        InformationItem(
                            text = "R${detailItem.weapon.affix_level}",
                            backgroundColor = GachaStar5Color,
                            textSize = 12.sp,
                            paddingValues = PaddingValues(
                                3.dp,
                                1.25.dp
                            )
                        )
                    }

                    Row {
                        Icon(
                            painter = painterResource(
                                id = FightProperty.getIconResourceByProperty(
                                    detailItem.weapon.main_property.property_type
                                )
                            ),
                            contentDescription = ""
                        )

                        PrimaryText(text = detailItem.weapon.main_property.final, fontSize = 12.sp)

                        Icon(
                            painter = painterResource(
                                id = FightProperty.getIconResourceByProperty(
                                    detailItem.weapon.sub_property.property_type
                                )
                            ),
                            contentDescription = ""
                        )

                        PrimaryText(text = detailItem.weapon.main_property.final, fontSize = 12.sp)

                    }

                }

            }

        }
    }

}