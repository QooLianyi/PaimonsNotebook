package com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen

import android.content.Intent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.extension.data_store.editValue
import com.lianyi.paimonsnotebook.common.extension.scope.launchIO
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.MaterialService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ElementType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.AvatarSkillFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemContentFilterHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemSearchOptionHelper
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.filter.ItemFilterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvatarScreenViewModel : ViewModel() {

    //当前角色
    var currentItem by mutableStateOf<AvatarData?>(null)

    //比对的角色
    var compareItem by mutableStateOf<AvatarData?>(null)

    //加载状态
    var loadingState by mutableStateOf(LoadingState.Loading)

    //属性列表
    var propertyList by mutableStateOf(listOf<FightPropertyFormat>())
        private set

    var compareItemPropertyList by mutableStateOf(listOf<FightPropertyFormat>())
        private set

    //技能列表
    var skillList by mutableStateOf(listOf<AvatarSkillFormat>())
        private set

    //命座列表
    var talentList by mutableStateOf(listOf<AvatarSkillFormat>())
        private set

    //材料列表
    var materialList by mutableStateOf(listOf<Material>())
        private set


    val itemFilterViewModel by lazy {
        ItemFilterViewModel(
            items = avatarService.avatarList,
            searchOptionList = ItemSearchOptionHelper
                .apply {
                    setListLayout()
                    setOrderBy(
                        options = listOf(
                            ItemFilterType.Default,
                            ItemFilterType.BaseATK,
                            ItemFilterType.BaseHp,
                            ItemFilterType.BaseDef,
                            ItemFilterType.BirthDay,
                            ItemFilterType.CostumeCount
                        )
                    )
                    setStar()
                    setWeapon()
                    setElement()
                    setAssociation()
                }.get(),
            getFilteredItemList = this::filterItemList,
            itemSortCompareBy = { avatar, type ->
                ItemContentFilterHelper.getAvatarGroupByKeyByType(
                    type = type,
                    avatar = avatar,
                    fightPropertyValueCalculateService = avatarService.fightPropertyValueCalculateService
                )
            }
        )
    }

    //当缺少文件时
    private fun onMissingFile() {
        loadingState = LoadingState.Error
    }

    private var currentItemLevel = 1

    //是否选择对比角色
    var selectCompareItem = false

    private val avatarService by lazy {
        AvatarService {
            onMissingFile()
        }
    }

    private val materialService by lazy {
        MaterialService {
            onMissingFile()
        }
    }

    fun init(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            val avatar = ItemHelper.getItemFromIntent(intent, avatarService.avatarList) { it.id }

            onClickItem(avatar)

            if (loadingState == LoadingState.Loading) {
                loadingState = LoadingState.Success
            }
        }
    }

    val tabs = arrayOf(
        "属性",
        "技能",
        "命之座",
        "资料",
        "材料"
    )

    //更新面板
    private fun updateProperty(promoted: Boolean = false) {
        if (currentItem == null) return

        propertyList = avatarService.getFightPropertyFormatList(
            currentItem!!,
            currentItemLevel,
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
        compareItemPropertyList = avatarService.getFightPropertyFormatList(
            compareItem!!,
            currentItemLevel,
            promoted
        )
    }

    //更新技能
    private fun updateSkill() {
        val list = mutableListOf<AvatarSkillFormat>()

        currentItem!!.skillDepot.Skills.forEachIndexed { index, skill ->
            list += AvatarSkillFormat.getValueForSkill(
                skill,
                if (index == 0) "普通攻击" else "元素战技"
            )
        }

        list += AvatarSkillFormat.getValueForEnergySkill(currentItem!!.skillDepot.EnergySkill)

        list += currentItem!!.skillDepot.Inherents.map {
            AvatarSkillFormat.getValueForInherent(it)
        }

        skillList = list
    }

    //更新命之座
    private fun updateTalent() {
        talentList = currentItem?.skillDepot?.Talents?.mapIndexed { index, talent ->
            AvatarSkillFormat.getValueForTalent(talent, "第${index + 1}层")
        } ?: listOf()
    }

    //更新材料
    private fun updateMaterial() {
        materialList = materialService.getMaterialListByIds(currentItem!!.cultivationItems)
    }

    //重置比对角色
    private fun resetCompareItem() {
        compareItem = null
        updateCompareAvatarProperty()
        selectCompareItem = false
    }

    //当点击角色时
    fun onClickItem(avatarData: AvatarData) {
        if (avatarData.id == currentItem?.id) return

        if (selectCompareItem) {
            compareItem = avatarData
            updateCompareAvatarProperty()
            selectCompareItem = false

            itemFilterViewModel.showResultList()
        } else {
            currentItem = avatarData
            resetCompareItem()

            updateProperty()
            updateSkill()
            updateTalent()

            updateMaterial()
        }

        itemFilterViewModel.dismissFilterContent()

        viewModelScope.launchIO {
            PreferenceKeys.LastViewAvatarId.editValue(avatarData.id)
        }
    }

    //当点击比对角色时
    fun onClickCompareItem() {
        selectCompareItem = compareItem == null

        if (selectCompareItem) {
            toggleFilterContent()
        } else {
            resetCompareItem()
        }
    }

    //当更改等级时
    fun onChangeItemLevel(value: Int) {
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

    //获取角色显示的数据内容
    fun getItemDataContent(
        avatarData: AvatarData,
        type: ItemFilterType,
        isList: Boolean = false
    ): String {
        val dataContent = ItemContentFilterHelper.getAvatarShowContentByType(
            type = type,
            avatar = avatarData,
            fightPropertyValueCalculateService = avatarService.fightPropertyValueCalculateService
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

    //过滤角色列表
    private fun filterItemList(items: List<AvatarData>): List<AvatarData> {
        val list = mutableListOf<AvatarData>()

        items.forEach { avatarData ->

            val fetterInfo = avatarData.fetterInfo

            itemFilterViewModel.apply {

                if (!filterValueExists(
                        ItemFilterType.Association,
                        fetterInfo.Association
                    )
                ) return@forEach

                if (!filterValueExists(ItemFilterType.Weapon, avatarData.weapon)) return@forEach

                val elementType = ElementType.getElementTypeByName(fetterInfo.VisionBefore)
                if (!filterValueExists(ItemFilterType.Element, elementType)) return@forEach

                if (!filterValueExists(ItemFilterType.Star, avatarData.starCount)) return@forEach

                //匹配名称与称号
                if (inputNameValue.isNotEmpty() &&
                    avatarData.name.indexOf(inputNameValue) == -1 &&
                    fetterInfo.Title.indexOf(inputNameValue) == -1
                ) {
                    return@forEach
                }
            }

            list += avatarData
        }

        return list
    }
}