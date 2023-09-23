package com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.hutao.AvatarService
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.hutao.MaterialService
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.hutao.WeaponService
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.QualityType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Materials
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import com.lianyi.paimonsnotebook.ui.screen.items.util.ItemHelper
import com.lianyi.paimonsnotebook.ui.screen.items.view.AvatarScreen
import com.lianyi.paimonsnotebook.ui.screen.items.view.WeaponScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class CultivationMaterialScreenViewModel : ViewModel() {

    var loadingState by mutableStateOf(LoadingState.Loading)
        private set

    private val materialService by lazy {
        MaterialService {
            onMissingFile()
        }
    }

    private val avatarService by lazy {
        AvatarService {
            onMissingFile()
        }
    }

    private val weaponService by lazy {
        WeaponService {
            onMissingFile()
        }
    }

    private fun onMissingFile() {
        loadingState = LoadingState.Error
    }

    var avatarList by mutableStateOf(listOf<AvatarData>())
        private set
    var weaponList by mutableStateOf(listOf<WeaponData>())
        private set

    var weekName by mutableStateOf("")
        private set

    init {
        setWeekData(LocalDateTime.now().dayOfWeek.value)
    }

    val dropMenuList by lazy {
        (1..7).map {
            TimeHelper.getWeekName(it) to it
        }
    }

    private fun setWeekData(week: Int, ignoreHour: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {

            Materials.getMaterialsIdByWeek(week, ignoreHour).also { result ->
                val pair = if (result.second != 7) {
                    val itemMap = materialService.getMaterialListByIds(result.first)
                        .filter { it.RankLevel == QualityType.QUALITY_PURPLE || it.RankLevel == QualityType.QUALITY_ORANGE }
                        .associateBy { it.Id }

                    weaponService.weaponList.filter { weaponData ->
                        weaponData.cultivationItems.takeFirstIf { id -> itemMap[id] != null } != null
                    } to avatarService.avatarList.filter { avatarData ->
                        avatarData.cultivationItems.takeFirstIf { id -> itemMap[id] != null } != null
                    }
                } else {
                    weaponService.weaponList to avatarService.avatarList
                }

                weaponList = pair.first.sortedByDescending { it.rankLevel }
                avatarList = pair.second.sortedByDescending { it.starCount }

                if (loadingState == LoadingState.Loading) {
                    viewModelScope.launch(Dispatchers.Main) {
                        weekName = TimeHelper.getWeekName(result.second)
                        loadingState = LoadingState.Success
                    }
                }
            }
        }
    }

    var showDropMenu by mutableStateOf(false)

    fun onSelectDropMenuItem(pair: Pair<String, Int>) {
        loadingState = LoadingState.Loading
        dismissDropMenu()
        setWeekData(pair.second, ignoreHour = true)
    }

    fun showDropMenu() {
        showDropMenu = true
    }

    fun dismissDropMenu() {
        showDropMenu = false
    }

    fun onClickAvatar(avatarData: AvatarData) {
        HomeHelper.goActivity(AvatarScreen::class.java, Bundle().apply {
            putInt(ItemHelper.PARAM_INT_ITEM_ID, avatarData.id)
        })
    }

    fun onClickWeapon(weaponData: WeaponData) {
        HomeHelper.goActivity(WeaponScreen::class.java, Bundle().apply {
            putInt(ItemHelper.PARAM_INT_ITEM_ID, weaponData.id)
        })
    }
}