package com.lianyi.paimonsnotebook.ui.screen.gacha.service

import com.lianyi.paimonsnotebook.common.database.PaimonsNotebookDatabase
import com.lianyi.paimonsnotebook.common.database.gacha.data.GachaRecordOverview
import com.lianyi.paimonsnotebook.common.util.data_store.PreferenceKeys
import com.lianyi.paimonsnotebook.common.util.data_store.dataStoreValues
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GachaRecordService {
    private val dao = PaimonsNotebookDatabase.database.gachaItemsDao

    //祈愿记录总览数据流
    private val GachaRecordOverviewListFlow =
        MutableStateFlow<List<GachaRecordOverview>>(listOf())

    //对外开放的祈愿记录数据流
    val gachaRecordOverviewListFlow = GachaRecordOverviewListFlow.asStateFlow()

    private val GachaRecordOverviewForCurrentUidFlow =
        MutableStateFlow<GachaRecordOverview?>(GachaRecordOverview("", listOf()))

    //对外开放的当前祈愿用户记录总览flow
    val gachaRecordOverviewForCurrentUidFlow = GachaRecordOverviewForCurrentUidFlow.asStateFlow()

    //当前祈愿用户数据流
    private val CurrentGachaRecordGameUidFlow = MutableStateFlow("")

    init {
        CoroutineScope(Dispatchers.IO).launch {
            launch {
                dataStoreValues {
                    val gameUid = it[PreferenceKeys.GachaRecordCurrentGameUid] ?: ""

                    println("gameUid = ${gameUid}")

                    CurrentGachaRecordGameUidFlow.value = gameUid
                    updateData()
                }
            }
        }
    }

    suspend fun updateData() {
        setGachaRecordOverviewForCurrentUidFlow()
    }

    private suspend fun setGachaRecordOverviewForCurrentUidFlow() {
        val list = dao.getOverviewsByUid(CurrentGachaRecordGameUidFlow.value)

        if (list.isEmpty()){
            GachaRecordOverviewForCurrentUidFlow.emit(null)
            return
        }

        val overviewItemList = mutableListOf<GachaRecordOverview.Item>()

        val starWeight = listOf(
            3 to 1f,
            4 to 10f,
            5 to 90f
        )

        list.groupBy { it.uigfGachaType }.forEach { (type, gachaRecordOverviewItems) ->

            val types = gachaRecordOverviewItems.groupBy { it.rankType }

            val gachaTimesMap = mutableMapOf<Int, Int>()
            val gachaProgressMap = mutableMapOf<Int, Float>()
            val countMap = mutableMapOf<Int, Int>()

            var minTime = ""
            var maxTime = ""

            starWeight.map { pair ->
                types["${pair.first}"]?.forEach {
                    val count = it.count
                    val star = pair.first

                    gachaProgressMap += star to (it.gachaTimes / pair.second)
                    gachaTimesMap += star to it.gachaTimes
                    countMap += star to count

                    maxTime = it.maxTime
                    minTime = it.minTime
                }
            }

            overviewItemList += GachaRecordOverview.Item(
                uigfGachaType = type,
                gachaTimesMap = gachaTimesMap,
                gachaProgressMap = gachaProgressMap,
                countMap = countMap,
                maxTime = maxTime,
                minTime = minTime
            )
        }
        overviewItemList.sortBy { it.uigfGachaType }

        GachaRecordOverviewForCurrentUidFlow.emit(
            GachaRecordOverview(
                uid = CurrentGachaRecordGameUidFlow.value,
                list = overviewItemList
            )
        )
    }

    suspend fun getHistoryWishByUIGFGachaTypeAndUid(uigfGachaType: String, uid: String) =
        dao.getHistoryWishByUIGFGachaTypeAndUid(uigfGachaType = uigfGachaType, uid = uid)

    //获取记录总览
    private suspend fun getGachaRecordOverviewByUid() {
        println("开始获取总览")
        val map = dao.getOverviews().groupBy { it.uid }

        val list = mutableListOf<GachaRecordOverview>()

        println("map ${map.keys.toList()}")
        //提交祈愿总览
        GachaRecordOverviewListFlow.emit(list)
        println("list size = ${list.size}")
        println("获取结束")
    }

}