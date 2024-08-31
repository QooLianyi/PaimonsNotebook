package com.lianyi.paimonsnotebook.ui.screen.gacha.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lianyi.paimonsnotebook.common.database.gacha.data.GachaRecordOverview
import com.lianyi.paimonsnotebook.common.util.enums.LoadingState
import com.lianyi.paimonsnotebook.common.util.metadata.genshin.uigf.UIGFHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.WeaponService
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.GachaOverviewListItem
import com.lianyi.paimonsnotebook.ui.screen.gacha.service.GachaRecordService
import com.lianyi.paimonsnotebook.ui.screen.gacha.view.GachaRecordOptionScreen
import com.lianyi.paimonsnotebook.ui.screen.home.util.HomeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GachaRecordScreenViewModel : ViewModel() {

    var currentPageIndex by mutableIntStateOf(0)

    val tabs = arrayOf("总览", "角色", "武器")

    var loadingState by mutableStateOf(LoadingState.Loading)

    var gachaRecordOverview by mutableStateOf<GachaRecordOverview?>(null)
        private set

    var overviewItemMap by mutableStateOf<Map<String, List<GachaOverviewListItem>>>(mapOf())
        private set

    private val gachaRecordService = GachaRecordService()

    val itemsList = mutableStateListOf<Pair<Int, List<Pair<GachaOverviewListItem, Int>>>>()

    init {
        viewModelScope.launch {
            //总览数据流
            launch {
                gachaRecordService.gachaRecordOverviewForCurrentUidFlow.collect { overview ->
                    println("overview = ${overview}")
                    gachaRecordOverview = overview

                    loadingState = if (overview == null) {
                        LoadingState.Empty
                    } else if (overview.uid.isEmpty() && overview.list.isEmpty()) {
                        LoadingState.Loading
                    } else if (overview.uid.isNotEmpty() && overview.list.isNotEmpty()) {
                        setPlayerGachaRecordList(gachaRecordOverview!!.uid)

                        LoadingState.Success
                    } else {
                        LoadingState.Empty
                    }
                }
            }
        }
    }

    private val weaponService by lazy {
        WeaponService {
            loadingState = LoadingState.Error
        }
    }

    private val avatarNameService by lazy {
        AvatarService {
            loadingState = LoadingState.Error
        }
    }

    private val weaponNameMap by lazy {
        weaponService.weaponList.associateBy {
            it.name
        }
    }

    private val avatarMap by lazy {
        avatarNameService.avatarList.associateBy {
            it.name
        }
    }

    private suspend fun setPlayerGachaRecordList(uid: String) {
        withContext(Dispatchers.IO) {
            val map = mutableMapOf<String, List<GachaOverviewListItem>>()

            val items = mutableMapOf<String, Pair<GachaOverviewListItem, Int>>()

            UIGFHelper.gachaList.forEach { type ->
                val list = gachaRecordService.getHistoryWishByUIGFGachaTypeAndUid(
                    uigfGachaType = type,
                    uid = uid
                )
                val star5Items = mutableListOf<GachaOverviewListItem>()

                var count = 0
                list.forEach { item ->
                    count++

                    val iconUrl = when (item.itemType) {
                        UIGFHelper.ItemType.Weapon -> {
                            weaponNameMap[item.name]?.iconUrl
                        }

                        UIGFHelper.ItemType.Avatar -> {
                            avatarMap[item.name]?.iconUrl
                        }

                        else -> null
                    }

                    val data = GachaOverviewListItem(
                        name = item.name,
                        iconUrl = iconUrl ?: "",
                        rankType = item.rankType.toIntOrNull() ?: 0,
                        count = count,
                        type = item.itemType
                    )

                    val pair = items[data.name]
                    if (pair == null) {
                        items[data.name] = data to 1
                    } else {
                        items[data.name] = pair.copy(second = pair.second + 1)
                    }

                    if ((item.rankType.toIntOrNull() ?: 0) == 5) {
                        star5Items += data
                        count = 0
                    }
                }

                if (star5Items.isNotEmpty()) {
                    map += type to star5Items.reversed()
                }
            }

            itemsList.clear()

            itemsList += items.toList().map {
                it.second
            }.toMutableList().sortedByDescending { it.second }.groupBy {
                it.first.rankType
            }.toList()
                .toMutableList()
                .sortedByDescending { it.first }

            overviewItemMap = map
        }
    }

    fun setSelectedPageIndex(pageIndex: Int) {
        this.currentPageIndex = pageIndex
    }

    fun goOptionScreen() {
        HomeHelper.goActivity(GachaRecordOptionScreen::class.java)
    }

}