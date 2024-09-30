package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquaryData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquarySetData
import org.json.JSONArray
import java.io.File

class ReliquaryService(onMissingFile: () -> Unit) {

    //圣遗物套装列表
    var reliquarySetList = listOf<ReliquarySetData>()
        private set

    //key = setId , value = 该套装的全部圣遗物
    var reliquarySetListMap = mapOf<Int, List<ReliquaryData>>()
        private set

    //key = reliquaryId , value = reliquaryData
    var reliquaryMap = mapOf<Int, ReliquaryData>()
        private set

    //key = setId , value = setData
    var reliquarySetMap = mapOf<Int, ReliquarySetData>()
        private set

    //key = setId , value = maxRankLevel
    var reliquaryMaxStarMap = mapOf<Int, Int>()
        private set

    init {
        val reliquaryFile = FileHelper.getMetadata(MetadataHelper.FileNameReliquary)
        val reliquarySetFile = FileHelper.getMetadata(MetadataHelper.FileNameReliquarySet)

        if (reliquaryFile == null || reliquarySetFile == null) {
            onMissingFile.invoke()
        } else {
            setReliquaryList(reliquaryFile)
            setReliquarySetMap(reliquarySetFile)
        }
    }

    private fun setReliquaryList(file: File) {
        val list = mutableListOf<ReliquaryData>()
        val maxStarMap = mutableMapOf<Int, Int>()
        val reliquaryMap = mutableMapOf<Int, ReliquaryData>()
        val jsonArray = JSONArray(file.readText())

        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            val item = JSON.parse<ReliquaryData>(jsonString)
            list += item
        }

        this.reliquarySetListMap = list.groupBy {
            it.SetId
        }.mapValues { entry ->
            //只保留最高等级的圣遗物
            val maxRankLevel = entry.value.maxOf { it.RankLevel }

            //记录最大星级
            maxStarMap[entry.key] = maxRankLevel
            entry.value.filter { it.RankLevel == maxRankLevel }.sortedBy { it.EquipType }.apply {
                this.forEach { reliquary ->
                    val lastId = reliquary.Ids.last()
                    reliquaryMap[lastId] = reliquary
                }
            }
        }
        this.reliquaryMaxStarMap = maxStarMap
        this.reliquaryMap = reliquaryMap
    }

    private fun setReliquarySetMap(file: File) {
        val list = mutableListOf<ReliquarySetData>()
        val jsonArray = JSONArray(file.readText())

        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            val item = JSON.parse<ReliquarySetData>(jsonString)
            list += item
        }

        this.reliquarySetList = list
        this.reliquarySetMap = list.associateBy { it.SetId }
    }

}