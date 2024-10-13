package com.lianyi.paimonsnotebook.ui.screen.items.util

import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.web.hoyolab.takumi.game_record.character.CharacterListData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.FightPropertyValueCalculateService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.AssociationType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.FightProperty
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.WeaponType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.wiki.PropertyCurveValue
import java.time.LocalDateTime
import java.time.ZoneOffset
import kotlin.math.roundToInt

object ItemContentFilterHelper {
    //获取排序类型名称通过类型
    fun getSortTypeNameByType(type: ItemFilterType?) =
        when (type) {
            ItemFilterType.Default -> "默认"
            ItemFilterType.Name -> "名称"
            ItemFilterType.BaseHp -> "基础生命值"
            ItemFilterType.BaseATK -> "基础攻击力"
            ItemFilterType.BaseDef -> "基础防御力"
            ItemFilterType.CostumeCount -> "服装数量"
            ItemFilterType.Element -> "元素类型"
            ItemFilterType.Association -> "区域"
            ItemFilterType.Star -> "星级"
            ItemFilterType.Weapon -> "武器"
            ItemFilterType.BirthDay -> "生日"

            ItemFilterType.Level -> "等级"
            ItemFilterType.Fetter -> "好感等级"
            ItemFilterType.ActiveConstellation -> "激活的命之座"
            else -> ""
        }

    private fun getWeaponPropertyValue(
        property: Int,
        weapon: WeaponData,
        fightPropertyValueCalculateService: FightPropertyValueCalculateService
    ): Int {
        val growCurve = weapon.growCurves.takeFirstIf {
            it.Type == property
        }

        return getItemPropertyValue(
            promoteId = weapon.promoteId,
            property = property,
            growCurveValue = growCurve?.Value ?: 0,
            value = growCurve?.InitValue ?: 0f,
            fightPropertyValueCalculateService = fightPropertyValueCalculateService
        )
    }


    private fun getAvatarPropertyValue(
        property: Int,
        avatar: AvatarData,
        fightPropertyValueCalculateService: FightPropertyValueCalculateService
    ): Int = getItemPropertyValue(
        promoteId = avatar.promoteId,
        property = property,
        growCurveValue = (avatar.growCurves.takeFirstIf { it.Type == property }?.Value) ?: 0,
        value = when (property) {
            FightProperty.FIGHT_PROP_BASE_HP -> avatar.baseValue.HpBase
            FightProperty.FIGHT_PROP_BASE_ATTACK -> avatar.baseValue.AttackBase
            FightProperty.FIGHT_PROP_BASE_DEFENSE -> avatar.baseValue.DefenseBase
            else -> 0f
        },
        fightPropertyValueCalculateService = fightPropertyValueCalculateService
    )

    private fun getItemPropertyValue(
        promoteId: Int,
        property: Int,
        growCurveValue: Int,
        value: Float,
        fightPropertyValueCalculateService: FightPropertyValueCalculateService
    ) = fightPropertyValueCalculateService.calculateFightProperty(
        propertyCurveValue = PropertyCurveValue(
            Property = property,
            Type = growCurveValue,
            Value = value
        ),
        promoted = true,
        level = 90,
        promoteId = promoteId
    ).roundToInt()

    fun getPlayerGroupByKeyByType(
        type: ItemFilterType,
        characterListData: CharacterListData.CharacterData
    ): Long = when (type) {
        ItemFilterType.Level -> characterListData.rarity * 10 + characterListData.level + characterListData.id
        ItemFilterType.Fetter -> characterListData.fetter * 100 + characterListData.rarity * 10 + characterListData.id
        ItemFilterType.ActiveConstellation -> characterListData.actived_constellation_num
        //默认排序,星级占比最大，其次是等级，最后是好感度与角色id
        //相同星级，等级，好感时，按照角色id大小排序
        ItemFilterType.Default -> characterListData.rarity * 10000000 + characterListData.level * 100000 + characterListData.fetter * 10000 + characterListData.id
        else -> 0
    }.toLong()

    fun getWeaponGroupByKeyByType(
        type: ItemFilterType,
        weapon: WeaponData,
        fightPropertyValueCalculateService: FightPropertyValueCalculateService
    ): Long =
        if (type == ItemFilterType.BaseATK) {
            val growCurve = weapon.growCurves.takeFirstIf {
                it.Type == FightProperty.FIGHT_PROP_BASE_ATTACK
            }

            getItemPropertyValue(
                promoteId = weapon.promoteId,
                property = FightProperty.FIGHT_PROP_BASE_ATTACK,
                growCurveValue = growCurve?.Value ?: 0,
                value = growCurve?.InitValue ?: 0f,
                fightPropertyValueCalculateService = fightPropertyValueCalculateService
            )
        } else {
            weapon.id
        }.toLong()

    //通过过滤类型获取排序key
    fun getAvatarGroupByKeyByType(
        type: ItemFilterType,
        avatar: AvatarData,
        fightPropertyValueCalculateService: FightPropertyValueCalculateService
    ): Long {
        return if (type == ItemFilterType.BirthDay) {
            //以东八区的时间转换生日的毫秒数,指定年份为1972(选择闰年以处理2月29日的情况)
            LocalDateTime.of(1972, avatar.fetterInfo.BirthMonth, avatar.fetterInfo.BirthDay, 0, 0)
                .toEpochSecond(ZoneOffset.of("+8"))
        } else {
            when (type) {
                ItemFilterType.Star -> {
                    avatar.starCount
                }

                ItemFilterType.BaseATK -> {
                    getAvatarPropertyValue(
                        FightProperty.FIGHT_PROP_BASE_ATTACK,
                        avatar,
                        fightPropertyValueCalculateService
                    )
                }

                ItemFilterType.BaseHp -> {
                    getAvatarPropertyValue(
                        FightProperty.FIGHT_PROP_BASE_HP,
                        avatar,
                        fightPropertyValueCalculateService
                    )
                }

                ItemFilterType.BaseDef -> {
                    getAvatarPropertyValue(
                        FightProperty.FIGHT_PROP_BASE_DEFENSE,
                        avatar,
                        fightPropertyValueCalculateService
                    )
                }

                ItemFilterType.CostumeCount -> {
                    avatar.costumes.size
                }

                else -> {
                    avatar.sort
                }
            }.toLong()
        }
    }

    fun getWeaponShowContentByType(
        type: ItemFilterType,
        weapon: WeaponData,
        fightPropertyValueCalculateService: FightPropertyValueCalculateService
    ) =
        when (type) {
            ItemFilterType.BaseATK -> "${
                getWeaponPropertyValue(
                    property = FightProperty.FIGHT_PROP_BASE_ATTACK,
                    weapon = weapon,
                    fightPropertyValueCalculateService = fightPropertyValueCalculateService
                )
            }"

            else -> weapon.name
        }

    //根据过滤类型获取显示的内容
    fun getAvatarShowContentByType(
        type: ItemFilterType,
        avatar: AvatarData,
        fightPropertyValueCalculateService: FightPropertyValueCalculateService
    ) =
        when (type) {
            ItemFilterType.Star -> {
                "${avatar.starCount}"
            }

            ItemFilterType.Weapon -> {
                WeaponType.getWeaponTypeName(avatar.weapon)
            }

            ItemFilterType.Element -> {
                avatar.fetterInfo.VisionBefore
            }

            ItemFilterType.Association -> {
                AssociationType.getAssociationNameByType(avatar.fetterInfo.Association)
            }

            ItemFilterType.BirthDay -> {
                "${avatar.fetterInfo.BirthMonth}月${avatar.fetterInfo.BirthDay}日"
            }

            ItemFilterType.Default, ItemFilterType.Name -> {
                avatar.name
            }

            ItemFilterType.BaseATK -> {
                "${
                    getAvatarPropertyValue(
                        FightProperty.FIGHT_PROP_BASE_ATTACK,
                        avatar,
                        fightPropertyValueCalculateService
                    )
                }"
            }

            ItemFilterType.BaseHp -> {
                "${
                    getAvatarPropertyValue(
                        FightProperty.FIGHT_PROP_BASE_HP,
                        avatar,
                        fightPropertyValueCalculateService
                    )
                }"
            }

            ItemFilterType.BaseDef -> {
                "${
                    getAvatarPropertyValue(
                        FightProperty.FIGHT_PROP_BASE_DEFENSE,
                        avatar,
                        fightPropertyValueCalculateService
                    )
                }"
            }

            ItemFilterType.CostumeCount -> {
                "${avatar.costumes.size}"
            }

            else -> {
                "${avatar.sort}"
            }
        }
}