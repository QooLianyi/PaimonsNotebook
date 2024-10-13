package com.lianyi.paimonsnotebook.ui.screen.player_character.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.components.media.NetworkImageForMetadata
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterListData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.items.components.information.InformationItem
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.StarGroup
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item.PlayerCharacterDetailTalentCard
import com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item.PlayerCharacterDetailWeaponCard
import com.lianyi.paimonsnotebook.ui.theme.Black
import com.lianyi.paimonsnotebook.ui.theme.CardBackGroundColor
import com.lianyi.paimonsnotebook.ui.theme.FetterColor
import com.lianyi.paimonsnotebook.ui.theme.White

@Composable
fun PlayerCharacterListCard(
    characterData: CharacterListData.CharacterData,
    getAvatarDataById: (Int) -> AvatarData?,
    getWeaponDataById: (Int) -> WeaponData?,
    getWeaponFightPropertyFormatList: (WeaponData, Int, Boolean) -> List<FightPropertyFormat>,
    onClick: (CharacterListData.CharacterData) -> Unit
) {
    val avatarData = remember(characterData.id) {
        getAvatarDataById.invoke(characterData.id)
    }

    val weaponData = remember(characterData.weapon.id) {
        getWeaponDataById.invoke(characterData.weapon.id)
    }

    //当武器或角色资料为空时,直接返回,不进行渲染
    if (avatarData == null || weaponData == null) {
        return
    }

    //武器战斗属性格式化列表
    val weaponFightPropertyFormatList = remember(weaponData.id) {
        getWeaponFightPropertyFormatList.invoke(weaponData, characterData.weapon.level, true)
    }

    Row(
        modifier = Modifier
            .radius(6.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(CardBackGroundColor)
            .clickable {
                onClick.invoke(characterData)
            }
            .padding(12.dp)
    ) {

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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

//            基本信息
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = avatarData.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Black
                )

                InformationItem(
                    text = "Lv.${characterData.level}",
                    backgroundColor = White,
                    textSize = 13.sp,
                    paddingValues = PaddingValues(
                        6.dp,
                        2.5.dp
                    )
                )

                InformationItem(
                    text = "${characterData.fetter}",
                    iconResId = R.drawable.icon_fetter,
                    backgroundColor = White,
                    textSize = 13.sp,
                    paddingValues = PaddingValues(
                        6.dp,
                        2.5.dp
                    ),
                    tint = FetterColor,
                    textColor = FetterColor
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
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

            //命座信息
            PlayerCharacterDetailTalentCard(
                talents = avatarData.skillDepot.Talents,
                elementTypeName = avatarData.fetterInfo.VisionBefore,
                activateCount = characterData.actived_constellation_num
            )


            //武器信息
            PlayerCharacterDetailWeaponCard(
                weaponData = weaponData,
                level = characterData.weapon.level,
                affixLevel = characterData.weapon.affix_level,
                weaponFightPropertyFormatList = weaponFightPropertyFormatList
            )
        }
    }
}