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
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.MaterialService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.AvatarSkillFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.ui.screen.items.data.cultivate.CultivateConfigData
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemContentFilterHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemFilterType
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemSearchOptionHelper
import com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.base.ItemBaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AvatarScreenViewModel : ItemBaseViewModel<AvatarData>() {
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

    val itemFilterViewModel by lazy {
        ItemSearchOptionHelper.getAvatarItemFilterViewModel(avatarService = avatarService)
    }

    private val avatarService by lazy {
        AvatarService {
            super.onMissingFile()
        }
    }

    private val materialService by lazy {
        MaterialService {
            onMissingFile()
        }
    }

    override fun init(intent: Intent) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = avatarService.avatarList

            if (list.isNotEmpty()) {
                val avatar = ItemHelper.getItemFromIntent(intent, list) { it.id }
                onClickItem(avatar)
            }

            if (loadingState == LoadingState.Loading) {
                loadingState = LoadingState.Success
            }
        }
    }

    override val tabs = arrayOf(
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

        list += AvatarSkillFormat.getValueForSkill(currentItem!!.skillDepot.EnergySkill, "元素爆发")

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
    override fun updateMaterial() {
        materialList.clear()
        materialList += materialService.getMaterialListByIds(currentItem!!.cultivationItems)
    }

    //重置比对角色
    private fun resetCompareItem() {
        compareItem = null
        updateCompareAvatarProperty()
        selectCompareItem = false
    }

    //当点击角色时
    override fun onClickItem(item: AvatarData) {
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
            updateTalent()

            updateMaterial()

            super.updateCurrentItemSelectedState(item.id)
            viewModelScope.launchIO {
                PreferenceKeys.LastViewAvatarId.editValue(item.id)
            }
        }

        itemFilterViewModel.dismissFilterContent()
    }

    //当点击比对角色时
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

    //获取角色显示的数据内容
    override fun getItemDataContent(
        item: AvatarData,
        type: ItemFilterType,
        isList: Boolean
    ): String {
        val dataContent = ItemContentFilterHelper.getAvatarShowContentByType(
            type = type,
            avatar = item,
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

    override fun getCurrentItemId(): Int = currentItem?.id ?: 0

    override fun onShowItemConfigDialog() {
        super.onShowItemConfigDialog()

        val avatar = this.currentItem ?: return

        //TODO 角色等级上限提升至100
        val avatarMaxLevel = 90
        val skillMaxLevel = 10

        cultivateConfigList += CultivateConfigData(
            name = "角色等级",
            iconUrl = avatar.iconUrl,
            maxLevel = avatarMaxLevel,
            tintIcon = false,
            type = CultivateItemType.Avatar,
            id = avatar.id,
            itemTypeId = avatar.fetterInfo.elementType
        ).apply {
            avatarEnergySkillId = avatar.skillDepot.EnergySkill.GroupId
        }

        cultivateConfigList += avatar.skillDepot.Skills.first().let { skill ->
            CultivateConfigData(
                name = "普通攻击",
                iconUrl = skill.iconUrl,
                maxLevel = skillMaxLevel,
                type = CultivateItemType.Skill,
                id = skill.GroupId
            )
        }

        cultivateConfigList += avatar.skillDepot.Skills.last().let { skill ->
            CultivateConfigData(
                name = "元素战技",
                iconUrl = skill.iconUrl,
                maxLevel = skillMaxLevel,
                type = CultivateItemType.Skill,
                id = skill.GroupId
            )
        }

        cultivateConfigList += avatar.skillDepot.EnergySkill.let { skill ->
            CultivateConfigData(
                name = "元素爆发",
                iconUrl = skill.iconUrl,
                maxLevel = skillMaxLevel,
                type = CultivateItemType.Skill,
                id = skill.GroupId
            )
        }
    }
}