package com.lianyi.paimonsnotebook.ui.screen.items.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemContentFilterHelper
import com.lianyi.paimonsnotebook.ui.theme.GachaStar5Color

/*
* 角色搜索排序数据
* */
class AvatarSearchFilterData {
    //搜索的名称
    var name by mutableStateOf("")

    //排序方式,正序/倒序
    var orderBy by mutableStateOf(
        SearchOptionData(
            sortBy = ItemFilterType.Default,
            initOrderBy = SortOrderBy.Descend,
            value = ItemFilterType.Default.ordinal
        )
    )

    //列表样式
    var avatarListLayoutStyle by mutableStateOf(ListLayoutStyle.ListVertical)

    //武器类型映射map,key = 武器类型
    private val weapon by lazy {
        mutableStateMapOf<Int, Int?>()
    }

    //元素类型映射map,key = 元素类型
    private val element by lazy {
        mutableStateMapOf<Int, Int?>()
    }

    //星级映射map,key = 星级
    private val star by lazy {
        mutableStateMapOf<Int, Int?>()
    }

    //地区映射map,key = 地区
    private val association by lazy {
        mutableStateMapOf<Int, Int?>()
    }

    //是否显示清除过滤器
    var showClearFilter by mutableStateOf(false)

    val searchOptionList by lazy {
        listOf(
            "列表布局" to listLayoutList,
            "排序" to orderByList,
            "星级" to starList,
            "武器类型" to weaponList,
            "元素类型" to elementList,
            "地区" to associationList,
        )
    }

    //列表布局
    private val listLayoutList by lazy {
        listOf(
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
    private val orderByList by lazy {
        val orderBy = SearchOptionData(
            sortBy = ItemFilterType.Default,
            initOrderBy = SortOrderBy.Descend,
            value = ItemFilterType.Default.ordinal
        )

        mutableListOf(
            orderBy
        ).apply {
            addAll(
                listOf(
                    ItemFilterType.BaseATK,
                    ItemFilterType.BaseHp,
                    ItemFilterType.BaseDef,
                    ItemFilterType.BirthDay,
                    ItemFilterType.CostumeCount
                ).map {
                    orderBy.copy(
                        name = ItemContentFilterHelper.getSortTypeNameByType(it),
                        value = it.ordinal
                    )
                }
            )
        }.toList()
    }

    //星级
    private val starList by lazy {
        listOf(
            SearchOptionData(sortBy = ItemFilterType.Star, value = 5, contentSlot = {
                StarGroup(
                    starCount = 5,
                    starTint = GachaStar5Color,
                    starSize = 18.dp
                )
            }),
            SearchOptionData(sortBy = ItemFilterType.Star, value = 4, contentSlot = {
                StarGroup(
                    starCount = 4,
                    starTint = GachaStar5Color,
                    starSize = 18.dp
                )
            })
        )
    }

    //武器类型
    private val weaponList by lazy {
        listOf(
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
    private val elementList by lazy {
        listOf(
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
    private val associationList by lazy {
        listOf(
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

    //重置筛选器
    fun resetFilter() {
        name = ""
        weapon.clear()
        element.clear()
        star.clear()
        association.clear()

        syncShowClearFilter()
    }

    //同步状态
    fun syncShowClearFilter() {
        showClearFilter =
            name.isNotEmpty() || weapon.keys.isNotEmpty() || element.isNotEmpty() || star.isNotEmpty() || association.isNotEmpty()
    }

    //选择筛选选项
    fun selectOption(optionData: SearchOptionData) {
        val optionValue = optionData.value

        val map = getMap(optionData.sortBy)

        if (map != null) {
            if (map[optionValue] == null) {
                map[optionValue] = optionValue
            } else {
                map.remove(optionValue)
            }
        }

        syncShowClearFilter()
    }

    //检查过滤条件的值,true为不符合条件
    fun checkValueFilterConditions(type: ItemFilterType, value: Int): Boolean {
        val map = getMap(type)
        return map != null && map.keys.isNotEmpty() && map[value] == null
    }

    //根据选择的筛选选项返回对应的map
    private fun getMap(type: ItemFilterType) =
        when (type) {
            ItemFilterType.Element -> element
            ItemFilterType.Weapon -> weapon
            ItemFilterType.Association -> association
            ItemFilterType.Star -> star
            else -> null
        }

    //获取选项选择状态
    fun getOptionSelectState(optionData: SearchOptionData) =
        when (optionData.sortBy) {
            ItemFilterType.Weapon,
            ItemFilterType.Element,
            ItemFilterType.Association,
            ItemFilterType.Star -> getMap(optionData.sortBy)?.get(optionData.value) != null

            ItemFilterType.ListLayout -> avatarListLayoutStyle.ordinal == optionData.value

            else -> orderBy == optionData
        }

}