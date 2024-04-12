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

                    CurrentGachaRecordGameUidFlow.value = gameUid
                    updateData()
                }
            }
        }
    }

    suspend fun updateData() {
        setGachaRecordOverviewForCurrentUidFlow()
    }

    //根据当前uid设置总览数据
    private suspend fun setGachaRecordOverviewForCurrentUidFlow() {
        val list = dao.getOverviewsByUid(CurrentGachaRecordGameUidFlow.value)

        if (list.isEmpty()) {
            GachaRecordOverviewForCurrentUidFlow.emit(null)
            return
        }

        val overviewItemList = mutableListOf<GachaRecordOverview.Item>()

        //设置星级权重
        val starWeight = listOf(
            3 to 1f,
            4 to 10f,
            5 to 90f
        )

        //使记录根据祈愿类型分类
        list.groupBy { it.uigfGachaType }.forEach { (type, gachaRecordOverviewItems) ->

            //使总览类型根据星级分组,key = 星级,value = 对应祈愿类型的星级总览数据
            val types = gachaRecordOverviewItems.associateBy { it.rankType }

            val gachaTimesMap = mutableMapOf<Int, Int>()
            val gachaProgressMap = mutableMapOf<Int, Float>()
            val countMap = mutableMapOf<Int, Int>()

            var minTime = ""
            var maxTime = ""

            //遍历星级,根据星级设置对应的数据
            starWeight.map { pair ->
                val overviewData = types["${pair.first}"] ?: return@forEach

                val count = overviewData.count
                val star = pair.first

                gachaProgressMap += star to (overviewData.gachaTimes / pair.second)
                gachaTimesMap += star to overviewData.gachaTimes
                countMap += star to count

                //获取最大时间与最小时间
                maxTime =
                    overviewData.maxTime.takeIf { it > maxTime || minTime.isEmpty() } ?: maxTime
                minTime =
                    overviewData.minTime.takeIf { it < minTime || minTime.isEmpty() } ?: minTime
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