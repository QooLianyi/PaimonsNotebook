package com.lianyi.paimonsnotebook.ui.screen.abyss.components.page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.common.components.layout.card.MaterialCard
import com.lianyi.paimonsnotebook.common.components.lazy.ContentSpacerLazyColumn
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingLayout
import com.lianyi.paimonsnotebook.common.components.loading.ContentLoadingPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.EmptyPlaceholder
import com.lianyi.paimonsnotebook.common.components.placeholder.ErrorPlaceholder
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.abyss.SpiralAbyssData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.monster.MonsterData
import com.lianyi.paimonsnotebook.ui.screen.abyss.components.information.AbyssOverviewInformationItem
import com.lianyi.paimonsnotebook.ui.screen.abyss.components.list_item.FloorItem
import com.lianyi.paimonsnotebook.ui.screen.abyss.components.list_item.ItemCard

@Composable
fun AbyssRecordPage(
    abyssData: SpiralAbyssData?,
    loadingState: LoadingState,
    getAvatarFromMetadata: (Int) -> AvatarData?,
    getMonsterFromMetadata: (String) -> MonsterData?
) {

    ContentLoadingLayout(
        loadingState = loadingState,
        loadingContent = {
            ContentLoadingPlaceholder(text = "正在获取数据...")
        },
        emptyContent = {
            EmptyPlaceholder("好像还没有战斗数据哦?")
        },
        errorContent = {
            ErrorPlaceholder("未登录或角色不存在")
        }, defaultContent = {
            ErrorPlaceholder("未登录或角色不存在")
        }
    ) {
        if (abyssData == null) return@ContentLoadingLayout
        Content(
            currentAbyssRecord = abyssData,
            getAvatarFromMetadata = getAvatarFromMetadata,
            getMonsterFromMetadata = getMonsterFromMetadata
        )
    }
}

@Composable
private fun Content(
    currentAbyssRecord: SpiralAbyssData,
    getAvatarFromMetadata: (Int) -> AvatarData?,
    getMonsterFromMetadata: (String) -> MonsterData?
) {
    ContentSpacerLazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        statusBarPaddingEnabled = false,
        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 6.dp)
    ) {
        item {
            MaterialCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {

                        if (currentAbyssRecord.defeat_rank.isNotEmpty()) {
                            val defeat = currentAbyssRecord.defeat_rank.first()
                            val defeatAvatar = remember {
                                getAvatarFromMetadata(defeat.avatar_id)
                            }

                            AbyssOverviewInformationItem(
                                "最多击破",
                                "${defeat.value}",
                                defeatAvatar?.iconUrl ?: defeat.avatar_icon
                            )
                        }

                        if (currentAbyssRecord.damage_rank.isNotEmpty()) {
                            val damage = currentAbyssRecord.damage_rank.first()
                            val damageAvatar = remember {
                                getAvatarFromMetadata(damage.avatar_id)
                            }

                            AbyssOverviewInformationItem(
                                "最强一击",
                                "${damage.value}",
                                damageAvatar?.iconUrl ?: damage.avatar_icon
                            )
                        }



                        if (currentAbyssRecord.take_damage_rank.isNotEmpty()) {
                            val takeDamage = currentAbyssRecord.take_damage_rank.first()
                            val takeDamageAvatar = remember {
                                getAvatarFromMetadata(takeDamage.avatar_id)
                            }

                            AbyssOverviewInformationItem(
                                "最多承伤",
                                "${takeDamage.value}",
                                takeDamageAvatar?.iconUrl ?: takeDamage.avatar_icon
                            )
                        }

                        if (currentAbyssRecord.normal_skill_rank.isNotEmpty()) {
                            val normalSkill =
                                currentAbyssRecord.normal_skill_rank.first()
                            val normalSkillAvatar = remember {
                                getAvatarFromMetadata(normalSkill.avatar_id)
                            }

                            AbyssOverviewInformationItem(
                                "元素战技次数",
                                "${normalSkill.value}",
                                normalSkillAvatar?.iconUrl ?: normalSkill.avatar_icon
                            )
                        }

                        if (currentAbyssRecord.energy_skill_rank.isNotEmpty()) {
                            val energySkill =
                                currentAbyssRecord.energy_skill_rank.first()
                            val energySkillAvatar = remember {
                                getAvatarFromMetadata(energySkill.avatar_id)
                            }

                            AbyssOverviewInformationItem(
                                "元素爆发次数",
                                "${energySkill.value}",
                                energySkillAvatar?.iconUrl ?: energySkill.avatar_icon
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AbyssOverviewInformationItem(
                            "最深抵达",
                            currentAbyssRecord.max_floor
                        )
                        AbyssOverviewInformationItem(
                            "战斗次数",
                            "${currentAbyssRecord.total_battle_times}"
                        )
                        AbyssOverviewInformationItem(
                            "获得渊星",
                            "${currentAbyssRecord.total_star}"
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        val avatars = remember {
                            currentAbyssRecord.reveal_rank.map {
                                Triple(
                                    getAvatarFromMetadata(it.avatar_id)?.iconUrl ?: it.avatar_icon,
                                    it.rarity,
                                    it.value
                                )
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            avatars.forEach { triple ->
                                ItemCard(
                                    url = triple.first,
                                    text = "${triple.third}",
                                    star = triple.second,
                                    width = 35.dp,
                                    height = 50.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        items(currentAbyssRecord.floors) { floor ->
            MaterialCard {
                FloorItem(
                    floor = floor,
                    getAvatarFromMetadata = getAvatarFromMetadata,
                    getMonsterDataFromMetadata = getMonsterFromMetadata
                )
            }
        }
    }
}