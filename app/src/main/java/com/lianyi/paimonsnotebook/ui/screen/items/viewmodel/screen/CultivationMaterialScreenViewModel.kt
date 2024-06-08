package com.lianyi.paimonsnotebook.ui.screen.items.viewmodel.screen

import android.os.Bundle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.data.popup.IconTitleInformationPopupWindowData
import com.lianyi.paimonsnotebook.common.data.popup.InformationPopupPositionProvider
import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.time.TimeHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.MaterialService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.WeaponService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
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

    init {
        setWeekData(LocalDateTime.now().dayOfWeek.value)
    }

    private val weaponService by lazy {
        WeaponService {
            onMissingFile()
        }
    }

    private fun onMissingFile() {
        loadingState = LoadingState.Error
    }

    var avatarList by mutableStateOf<List<Pair<List<Material>, List<AvatarData>>>>(listOf())
        private set
    var weaponList by mutableStateOf<List<Pair<List<Material>, List<WeaponData>>>>(listOf())
        private set

    var weekName by mutableStateOf("")
        private set


    val dropMenuList by lazy {
        (1..7).map {
            TimeHelper.getWeekName(it) to it
        }
    }

    var currentPageIndex by mutableIntStateOf(0)
        private set

    val tabs by lazy {
        arrayOf(
            "天赋培养", "武器突破"
        )
    }

    var showMaterialPopupWindow by mutableStateOf(false)
        private set

    var popupWindowProvider by mutableStateOf(InformationPopupPositionProvider())
        private set

    lateinit var popupWindowData: IconTitleInformationPopupWindowData

    fun onChangePage(value: Int) {
        currentPageIndex = value
    }

    private fun setWeekData(week: Int, ignoreHour: Boolean = false) {
        viewModelScope.launch(Dispatchers.IO) {

            Materials.getMaterialsIdByWeek(week, ignoreHour).also { result ->
                val materialMap = materialService.getMaterialListByIds(result.first)
                    .associateBy { it.Id }

                weaponList = formatDataList(
                    materialMap = materialMap,
                    list = weaponService.weaponList,
                    groupByCondition = {
                        val id = it.cultivationItems
                            .takeFirstIf { id -> materialMap[id] != null }?.let { value ->
                                value + if (it.rankLevel < 3) 1 else 0
                            }
                        materialMap[id]
                    }, listSortCondition = {
                        it.rankLevel
                    }, typeMaterialCount = 4
                )

                avatarList = formatDataList(
                    materialMap = materialMap,
                    list = avatarService.avatarList,
                    groupByCondition = {
                        materialMap[it.cultivationItems.takeFirstIf { id -> materialMap[id] != null }]
                    }, listSortCondition = {
                        it.starCount
                    }, typeMaterialCount = 3
                )

                if (loadingState == LoadingState.Loading) {
                    viewModelScope.launch(Dispatchers.Main) {
                        weekName = TimeHelper.getWeekName(result.second)
                        loadingState = LoadingState.Success
                    }
                }
            }
        }
    }

    /*
    * 格式化数据列表
    *
    * materialMap:材料map
    * list:数据列表
    * groupByCondition:分组条件
    * listSortCondition:列表排序条件
    * typeMaterialCount:类型材料个数
    * */
    private fun <T> formatDataList(
        materialMap: Map<Int, Material>,
        list: List<T>,
        groupByCondition: (T) -> Material?,
        listSortCondition: (T) -> Int,
        typeMaterialCount: Int
    ) =
        list.asSequence().filter {
            groupByCondition.invoke(it) != null
        }.groupBy(groupByCondition).toList()
            .sortedByDescending { it.first?.RankLevel ?: 0 }
            .map { pair ->
                val material = pair.first
                val typeList = mutableListOf<Material>()

                if (material != null) {
                    repeat(typeMaterialCount) {
                        val item = materialMap[material.Id - it]
                        if (item != null) {
                            typeList += item
                        }
                    }
                }
                typeList to pair.second.sortedByDescending(listSortCondition)
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

    fun dismissPopupWindow() {
        showMaterialPopupWindow = false
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

    fun onClickMaterialItem(material: Material, intSize: IntSize, offset: Offset) {
        popupWindowProvider = InformationPopupPositionProvider(
            contentOffset = offset,
            itemSize = intSize,
            itemSpace = 8.dp
        )

        popupWindowData = material.getShowPopupWindowInfo()

        showMaterialPopupWindow = true
    }

}