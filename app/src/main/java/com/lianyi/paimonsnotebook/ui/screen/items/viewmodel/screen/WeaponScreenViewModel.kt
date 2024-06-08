package com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.cultivate.data.CultivateItemType
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.MaterialService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.WeaponService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.WeaponAffixFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.items.data.cultivate.CultivateConfigData
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemContentFilterHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemSearchOptionHelper
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.base.ItemBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeaponScreenViewModel : ItemBaseViewModel<WeaponData>() {

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

    val itemFilterViewModel by lazy {
        ItemSearchOptionHelper.getWeaponFilterItemViewModel(weaponService = weaponService)
    }

    override fun init(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = weaponService.weaponList

            if (list.isNotEmpty()) {
                val weapon =
                    ItemHelper.getItemFromIntent(intent, weaponService.weaponList) { it.id }

                onClickItem(weapon)
            }

            if (loadingState == LoadingState.Loading) {
                loadingState = LoadingState.Success
            }
        }
    }

    override val tabs = arrayOf(
        "属性", "精炼", "资料", "材料"
    )

    //更新面板
    private fun updateProperty(promoted: Boolean = false) {
        if (currentItem == null) return

        propertyList = weaponService.getFightPropertyFormatList(
            currentItem!!, currentItemLevel, promoted
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
            compareItem!!, currentItemLevel, promoted
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
    override fun updateMaterial() {
        if (currentItem == null) return
        materialList.clear()

        materialList += materialService.getMaterialListByIds(currentItem!!.cultivationItems)
    }

    //重置比对角色
    private fun resetCompareItem() {
        compareItem = null
        updateCompareAvatarProperty()
        selectCompareItem = false
    }

    override fun onClickItem(item: WeaponData) {
        if (item.id == currentItem?.id) return

        if (selectCompareItem) {
            compareItem = item
            updateCompareAvatarProperty()
            selectCompareItem = false

            itemFilterViewModel.showResultList()
        } else {
            currentItem = item
            resetCompareItem()

            updateProperty()
            updateSkill()

            updateMaterial()
            viewModelScope.launchIO {
                PreferenceKeys.LastViewWeaponId.editValue(item.id)
            }
        }

        itemFilterViewModel.dismissFilterContent()


    }

    override fun onClickCompareItem() {
        selectCompareItem = compareItem == null

        if (selectCompareItem) {
            toggleFilterContent()
        } else {
            resetCompareItem()
        }
    }

    //当更改等级时
    override fun onChangeItemLevel(value: Int, promoted: Boolean) {
        currentItemLevel = value

        updateProperty(promoted)
        updateCompareAvatarProperty(promoted)
    }

    override fun onPromotedChange(promoted: Boolean) {
        updateProperty(promoted)
        updateCompareAvatarProperty(promoted)
    }

    override fun toggleFilterContent() {
        itemFilterViewModel.toggleFilterContent()

        if (!itemFilterViewModel.showFilterContent) {
            resetCompareItem()
        }
    }

    override fun getItemDataContent(
        item: WeaponData, type: ItemFilterType, isList: Boolean
    ): String {
        val dataContent = ItemContentFilterHelper.getWeaponShowContentByType(
            type = type,
            weapon = item,
            fightPropertyValueCalculateService = weaponService.fightPropertyValueCalculateService
        )

        return if (isList) {
            when (type) {
                ItemFilterType.Default, ItemFilterType.Name -> ""

                else -> "${ItemContentFilterHelper.getSortTypeNameByType(type)}:${dataContent}"
            }
        } else {
            dataContent
        }
    }

    override fun getCurrentItemId(): Int = currentItem?.id ?: 0

    override fun onShowItemConfigDialog() {
        super.onShowItemConfigDialog()

        val weapon = currentItem ?: return

        //TODO 适配武器等级为100
        val weaponMaxLevel = weapon.maxLevel

        cultivateConfigList += CultivateConfigData(
            name = "武器等级",
            iconUrl = weapon.iconUrl,
            id = weapon.id,
            type = CultivateItemType.Weapon,
            maxLevel = weaponMaxLevel,
            tintIcon = false
        )
    }
}