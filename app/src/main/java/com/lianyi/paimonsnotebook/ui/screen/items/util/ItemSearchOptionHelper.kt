package com.lianyi.paimonsnotebook.ui.screen.items.util

import androidx.compose.ui.unit.dp
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.common.util.enums.ListLayoutStyle
import com.lianyi.paimonsnotebook.common.util.enums.SortOrderBy
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.AssociationIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.WeaponTypeIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.AssociationType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ElementType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.WeaponType
import com.lianyi.paimonsnotebook.ui.screen.items.components.widget.StarGroup
import com.lianyi.paimonsnotebook.ui.screen.items.data.SearchOptionData
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color

object ItemSearchOptionHelper {
    private val list by lazy {
        mutableListOf<Pair<String, List<SearchOptionData>>>()
    }

    //列表布局
    fun setListLayout() {
        list += "列表布局" to listOf(
            SearchOptionData(
                sortBy = ItemFilterType.ListLayout,
                name = "列表",
                value = ListLayoutStyle.ListVertical.ordinal,
                iconResId = R.drawable.ic_list
            ),
            SearchOptionData(
                sortBy = ItemFilterType.ListLayout,
                name = "网格",
                value = ListLayoutStyle.GridVertical.ordinal,
                iconResId = R.drawable.ic_grid
            )
        )
    }

    //排序列表
    fun setOrderBy(
        options: List<ItemFilterType>
    ) {
        val orderBy = SearchOptionData(
            sortBy = ItemFilterType.Default,
            initOrderBy = SortOrderBy.Descend,
            value = ItemFilterType.Default.ordinal
        )

        list += "排序" to options.map {
            orderBy.copy(
                name = ItemContentFilterHelper.getSortTypeNameByType(it),
                value = it.ordinal
            )
        }
    }

    //星级
    fun setStar(starRange: IntProgression = 5 downTo 4) {
        list += "星级" to starRange.map {
            SearchOptionData(sortBy = ItemFilterType.Star, value = it, contentSlot = {
                StarGroup(
                    starCount = it,
                    starTint = GachaStar5Color,
                    starSize = 18.dp
                )
            })
        }
    }

    //武器类型
    fun setWeapon() {
        list += "武器" to listOf(
            SearchOptionData(
                sortBy = ItemFilterType.Weapon,
                iconUrl = WeaponTypeIconConverter.weaponTypeToIconUrl(WeaponType.WEAPON_SWORD_ONE_HAND),
                name = WeaponType.getWeaponTypeName(WeaponType.WEAPON_SWORD_ONE_HAND),
                value = WeaponType.WEAPON_SWORD_ONE_HAND
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Weapon,
                iconUrl = WeaponTypeIconConverter.weaponTypeToIconUrl(WeaponType.WEAPON_CLAYMORE),
                name = WeaponType.getWeaponTypeName(WeaponType.WEAPON_CLAYMORE),
                value = WeaponType.WEAPON_CLAYMORE
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Weapon,
                iconUrl = WeaponTypeIconConverter.weaponTypeToIconUrl(WeaponType.WEAPON_BOW),
                name = WeaponType.getWeaponTypeName(WeaponType.WEAPON_BOW),
                value = WeaponType.WEAPON_BOW
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Weapon,
                iconUrl = WeaponTypeIconConverter.weaponTypeToIconUrl(WeaponType.WEAPON_POLE),
                name = WeaponType.getWeaponTypeName(WeaponType.WEAPON_POLE),
                value = WeaponType.WEAPON_POLE
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Weapon,
                iconUrl = WeaponTypeIconConverter.weaponTypeToIconUrl(WeaponType.WEAPON_CATALYST),
                name = WeaponType.getWeaponTypeName(WeaponType.WEAPON_CATALYST),
                value = WeaponType.WEAPON_CATALYST
            )
        )
    }

    //元素类型
    fun setElement() {
        list += "元素" to listOf(
            SearchOptionData(
                sortBy = ItemFilterType.Element,
                name = ElementType.getElementNameByType(ElementType.Fire),
                iconResId = ElementType.getElementResourceIdByType(ElementType.Fire),
                value = ElementType.Fire
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Element,
                name = ElementType.getElementNameByType(ElementType.Water),
                iconResId = ElementType.getElementResourceIdByType(ElementType.Water),
                value = ElementType.Water
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Element,
                name = ElementType.getElementNameByType(ElementType.Grass),
                iconResId = ElementType.getElementResourceIdByType(ElementType.Grass),
                value = ElementType.Grass
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Element,
                name = ElementType.getElementNameByType(ElementType.Electric),
                iconResId = ElementType.getElementResourceIdByType(ElementType.Electric),
                value = ElementType.Electric
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Element,
                name = ElementType.getElementNameByType(ElementType.Ice),
                iconResId = ElementType.getElementResourceIdByType(ElementType.Ice),
                value = ElementType.Ice
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Element,
                name = ElementType.getElementNameByType(ElementType.Wind),
                iconResId = ElementType.getElementResourceIdByType(ElementType.Wind),
                value = ElementType.Wind
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Element,
                name = ElementType.getElementNameByType(ElementType.Rock),
                iconResId = ElementType.getElementResourceIdByType(ElementType.Rock),
                value = ElementType.Rock
            )
        )
    }

    //地区
    fun setAssociation() {
        list += "地区" to listOf(
            SearchOptionData(
                sortBy = ItemFilterType.Association,
                name = AssociationType.getAssociationNameByType(AssociationType.ASSOC_TYPE_MONDSTADT),
                iconUrl = AssociationIconConverter.avatarAssociationToUrl(AssociationType.ASSOC_TYPE_MONDSTADT),
                value = AssociationType.ASSOC_TYPE_MONDSTADT
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Association,
                name = AssociationType.getAssociationNameByType(AssociationType.ASSOC_TYPE_LIYUE),
                iconUrl = AssociationIconConverter.avatarAssociationToUrl(AssociationType.ASSOC_TYPE_LIYUE),
                value = AssociationType.ASSOC_TYPE_LIYUE
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Association,
                name = AssociationType.getAssociationNameByType(AssociationType.ASSOC_TYPE_INAZUMA),
                iconUrl = AssociationIconConverter.avatarAssociationToUrl(AssociationType.ASSOC_TYPE_INAZUMA),
                value = AssociationType.ASSOC_TYPE_INAZUMA
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Association,
                name = AssociationType.getAssociationNameByType(AssociationType.ASSOC_TYPE_SUMERU),
                iconUrl = AssociationIconConverter.avatarAssociationToUrl(AssociationType.ASSOC_TYPE_SUMERU),
                value = AssociationType.ASSOC_TYPE_SUMERU
            ),
            SearchOptionData(
                sortBy = ItemFilterType.Association,
                name = AssociationType.getAssociationNameByType(AssociationType.ASSOC_TYPE_FONTAINE),
                iconUrl = AssociationIconConverter.avatarAssociationToUrl(AssociationType.ASSOC_TYPE_FONTAINE),
                value = AssociationType.ASSOC_TYPE_FONTAINE
            ),
        )
    }

    fun get(): List<Pair<String, List<SearchOptionData>>> {
        val result = list.toList()
        list.clear()
        return result
    }
}