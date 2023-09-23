package com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.hutao.MaterialService
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.hutao.WeaponService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.WeaponAffixFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemContentFilterHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemSearchOptionHelper
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.filter.ItemFilterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeaponScreenViewModel : ViewModel() {
    private var currentItemLevel = 1f

    var currentItem by mutableStateOf<WeaponData?>(null)

    var compareItem by mutableStateOf<WeaponData?>(null)

    //加载状态
    var loadingState by mutableStateOf(LoadingState.Loading)

    var selectCompareItem = false

    private val weaponService by lazy {
        WeaponService {
            onMissingFile()
        }
    }

    private val materialService by lazy {
        MaterialService {
            onMissingFile()
        }
    }

    //属性列表
    var propertyList by mutableStateOf(listOf<FightPropertyFormat>())
        private set

    var compareItemPropertyList by mutableStateOf(listOf<FightPropertyFormat>())
        private set

    //技能列表
    var weaponAffixFormat by mutableStateOf<WeaponAffixFormat?>(null)
        private set

    //材料列表
    var materialList by mutableStateOf(listOf<Material>())
        private set

    //当缺少文件时
    private fun onMissingFile() {
        loadingState = LoadingState.Error
    }

    val itemFilterViewModel by lazy {
        ItemFilterViewModel(
            items = weaponService.weaponList,
            searchOptionList = ItemSearchOptionHelper
                .apply {
                    setListLayout()
                    setOrderBy(
                        options = listOf(
                            ItemFilterType.Default,
                            ItemFilterType.BaseATK,
                        )
                    )
                    setStar()
                    setWeapon()
                }.get(),
            getFilteredItemList = this::filterItemList,
            itemSortCompareBy = { weapon, type ->
                ItemContentFilterHelper.getWeaponGroupByKeyByType(
                    type,
                    weapon,
                    weaponService.fightPropertyValueCalculateService
                )
            }
        )
    }

    fun init(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            val weapon = ItemHelper.getItemFromIntent(intent, weaponService.weaponList) { it.id }

            onClickItem(weapon)

            if (loadingState == LoadingState.Loading) {
                loadingState = LoadingState.Success
            }
        }
    }

    val tabs = arrayOf(
        "属性",
        "精炼",
        "资料",
        "材料"
    )

    //更新面板
    private fun updateProperty(promoted: Boolean = false) {
        if (currentItem == null) return

        propertyList = weaponService.getFightPropertyFormatList(
            currentItem!!,
            currentItemLevel.toInt(),
            promoted
        )
    }

    private fun updateCompareAvatarProperty(promoted: Boolean = false) {
        if (compareItem == null) {
            if (compareItemPropertyList.isNotEmpty()) {
                compareItemPropertyList = listOf()
            }

            return
        }
        compareItemPropertyList = weaponService.getFightPropertyFormatList(
            compareItem!!,
            currentItemLevel.toInt(),
            promoted
        )
    }

    //更新技能
    private fun updateSkill() {
        if (currentItem == null) return

        val max = currentItem!!.affix?.Descriptions?.size ?: 1

        weaponAffixFormat = if (max != 1) {
            WeaponAffixFormat(currentItem?.affix)
        } else {
            null
        }
    }

    //更新材料
    private fun updateMaterial() {
        if (currentItem == null) return

        materialList = materialService.getMaterialListByIds(currentItem!!.cultivationItems)
    }

    //重置比对角色
    private fun resetCompareItem() {
        compareItem = null
        updateCompareAvatarProperty()
        selectCompareItem = false
    }

    fun onClickItem(weaponData: WeaponData) {
        if (weaponData.id == currentItem?.id) return

        if (selectCompareItem) {
            compareItem = weaponData
            updateCompareAvatarProperty()
            selectCompareItem = false

            itemFilterViewModel.showResultList()
        } else {
            currentItem = weaponData
            resetCompareItem()

            updateProperty()
            updateSkill()

            updateMaterial()
        }

        itemFilterViewModel.dismissFilterContent()
    }

    fun onClickCompareItem() {
        selectCompareItem = compareItem == null

        if (selectCompareItem) {
            toggleFilterContent()
        } else {
            resetCompareItem()
        }
    }

    //当更改等级时
    fun onChangeItemLevel(value: Float) {
        currentItemLevel = value

        updateProperty()
        updateCompareAvatarProperty()
    }

    fun onPromotedChange(promoted: Boolean) {
        updateProperty(promoted)
        updateCompareAvatarProperty(promoted)
    }

    fun toggleFilterContent() {
        itemFilterViewModel.toggleFilterContent()

        if (!itemFilterViewModel.showFilterContent) {
            resetCompareItem()
        }
    }

    fun getItemDataContent(
        weaponData: WeaponData,
        type: ItemFilterType,
        isList: Boolean = false
    ): String {
        val dataContent = ItemContentFilterHelper.getWeaponShowContentByType(
            type = type,
            weapon = weaponData,
            fightPropertyValueCalculateService = weaponService.fightPropertyValueCalculateService
        )

        return if (isList) {
            when (type) {
                ItemFilterType.Default,
                ItemFilterType.Name -> ""

                else -> "${ItemContentFilterHelper.getSortTypeNameByType(type)}:${dataContent}"
            }
        } else {
            dataContent
        }
    }

    private fun filterItemList(items: List<WeaponData>): List<WeaponData> {
        val list = mutableListOf<WeaponData>()

        items.forEach { weaponData ->

            itemFilterViewModel.apply {

                if (!filterValueExists(ItemFilterType.Weapon, weaponData.weaponType)) return@forEach

                if (!filterValueExists(ItemFilterType.Star, weaponData.rankLevel)) return@forEach

                //匹配名称与称号
                if (inputNameValue.isNotEmpty() &&
                    weaponData.name.indexOf(inputNameValue) == -1
                ) {
                    return@forEach
                }
            }

            list += weaponData
        }

        return list
    }
}