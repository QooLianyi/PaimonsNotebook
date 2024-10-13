package com.lianyi.paimonsnotebook.ui.screen.player_character.components.card.item

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lianyi.core.ui.components.text.PrimaryText
import com.lianyi.core.ui.components.text.RichText
import com.lianyi.paimonsnotebook.common.components.widget.Divider_10
import com.lianyi.paimonsnotebook.common.extension.modifier.radius.radius
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.SkillIconConverter
import com.lianyi.paimonsnotebook.ui.theme.White_40
import com.lianyi.paimonsnotebook.ui.theme.White_70

@Composable
fun PlayerCharacterDetailTalentCard(
    talents: List<AvatarData.Talent>,
    elementTypeName: String,
    activateCount: Int,
    backgroundColor: Color = White_70,
    clickable: Boolean = false,
) {
    var showIndex by remember {
        mutableIntStateOf(-1)
    }

    var showDesc by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .radius(4.dp)
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            talents.forEachIndexed { index, talent ->
                PlayerCharacterDetailSkillItem(
                    iconUrl = SkillIconConverter.iconNameToUrl(talent.Icon),
                    index = index,
                    elementTypeName = elementTypeName,
                    activateCount = activateCount,
                    clickable = clickable,
                    level = -1,
                    onClick = {
                        showDesc = showIndex != index || !showDesc
                        showIndex = index
                    }
                )
            }
        }

        AnimatedVisibility(visible = showDesc) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val data = talents[showIndex]

                PrimaryText(text = data.Name, textSize = 14.sp)

                RichText(text = data.Description, fontSize = 12.sp)
            }
        }
    }
}


@Composable
fun PlayerCharacterDetailSkillCard(
    skillDepot: AvatarData.SkillDepot,
    elementTypeName: String,
    skillLevelMap: Map<Int, Int>,
    backgroundColor: Color = White_70
) {
    var showIndex by remember {
        mutableIntStateOf(-1)
    }

    var showDesc by remember {
        mutableStateOf(false)
    }

    var name by remember {
        mutableStateOf("")
    }

    var desc by remember {
        mutableStateOf("")
    }

    val proudDescList = remember {
        mutableStateListOf<Pair<String, String>>()
    }

    val setNameAndDesc: (String, String, List<Pair<String, String>>) -> Unit = remember {
        { n, d, l ->
            if (showDesc) {
                name = n
                desc = d
                proudDescList.clear()
                proudDescList += l
            }
        }
    }

    Column(
        modifier = Modifier
            .radius(4.dp)
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            skillDepot.Skills.forEachIndexed { index, skill ->
                val level = skillLevelMap[skill.Id] ?: -1
                PlayerCharacterDetailSkillItem(
                    iconUrl = SkillIconConverter.iconNameToUrl(skill.Icon),
                    index = index,
                    elementTypeName = elementTypeName,
                    activateCount = -1,
                    level = level,
                    onClick = {
                        showDesc = showIndex != index || !showDesc
                        showIndex = index
                        setNameAndDesc.invoke(
                            skill.Name,
                            skill.Description,
                            skill.Proud.descriptions(level)
                        )
                    }
                )
            }

            val energySkillLevel = skillLevelMap[skillDepot.EnergySkill.Id] ?: -1

            PlayerCharacterDetailSkillItem(
                iconUrl = SkillIconConverter.iconNameToUrl(skillDepot.EnergySkill.Icon),
                index = skillDepot.Skills.size,
                elementTypeName = elementTypeName,
                activateCount = -1,
                level = energySkillLevel,
                onClick = {
                    showDesc = showIndex != skillDepot.Skills.size || !showDesc
                    showIndex = skillDepot.Skills.size

                    setNameAndDesc.invoke(
                        skillDepot.EnergySkill.Name,
                        skillDepot.EnergySkill.Description,
                        skillDepot.EnergySkill.Proud.descriptions(energySkillLevel)
                    )
                }
            )

            skillDepot.Inherents.forEachIndexed { index, inherent ->
                //需要将天赋id除以100以对应接口返回的数据
                val level = skillLevelMap[inherent.Id / 100] ?: -1
                PlayerCharacterDetailSkillItem(
                    iconUrl = SkillIconConverter.iconNameToUrl(inherent.Icon),
                    index = index,
                    elementTypeName = elementTypeName,
                    activateCount = -1,
                    level = level,
                    onClick = {
                        val cIndex = skillDepot.Skills.size + 1 + index
                        showDesc = showIndex != cIndex || !showDesc
                        showIndex = cIndex

                        setNameAndDesc.invoke(
                            inherent.Name,
                            inherent.Description,
                            inherent.Proud.descriptions(level)
                        )
                    }
                )
            }
        }

        AnimatedVisibility(visible = showDesc) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                PrimaryText(text = name, textSize = 14.sp)

                RichText(text = desc, fontSize = 12.sp)


                if (proudDescList.isNotEmpty()) {
                    Divider_10()
                }

                proudDescList.forEach { pair ->
                    Row(
                        modifier = Modifier
                            .radius(4.dp)
                            .fillMaxWidth()
                            .background(White_40)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = pair.first, fontSize = 12.sp)
                        Text(text = pair.second, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}


