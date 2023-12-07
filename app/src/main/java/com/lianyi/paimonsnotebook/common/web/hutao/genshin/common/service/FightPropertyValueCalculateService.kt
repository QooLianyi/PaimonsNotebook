package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service

import com.lianyi.paimonsnotebook.common.extension.list.takeFirstIf
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.GrowCurveData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.PromoteData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.format.FightPropertyFormat
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.wiki.PropertyCurveValue
import org.json.JSONArray
import java.io.File

class FightPropertyValueCalculateService(growCurveFile: File, promoteFile: File) {

    //成长曲线缓存 key = level
    private val growCurveMap = mutableMapOf<Int, GrowCurveData>()

    //突破加成缓存,key = promoteId
    private val promoteMap = mutableMapOf<Int, Map<Int, PromoteData>>()

    init {
        setGrowCurveMap(JSONArray(growCurveFile.readText()))
        setPromoteMap(JSONArray(promoteFile.readText()))
    }

    private fun setGrowCurveMap(jsonArray: JSONArray) {
        val list = mutableListOf<GrowCurveData>()
        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            list += JSON.parse<GrowCurveData>(jsonString)
        }

        growCurveMap.putAll(list.associateBy { it.Level })
    }

    private fun setPromoteMap(jsonArray: JSONArray) {
        val list = mutableListOf<PromoteData>()
        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            list += JSON.parse<PromoteData>(jsonString)
        }

        promoteMap.putAll(
            list.groupBy { promoteData ->
                promoteData.Id
            }.entries.associate { entry ->
                entry.key to entry.value.associateBy {
                    it.Level
                }
            }
        )
    }

    //通过突破id获取突破加成map
    fun getPromoteMapByPromoteId(promoteId: Int) = promoteMap[promoteId]

    //获取战斗属性格式化列表
    fun getFightPropertyFormatList(
        propertyCurveValues:List<PropertyCurveValue>,
        promoteId:Int,
        level:Int,
        promoted: Boolean,
    ):List<FightPropertyFormat>{
        return propertyCurveValues.map { propertyCurveValue ->
            val result = calculateFightProperty(
                propertyCurveValue = propertyCurveValue,
                promoteId = promoteId,
                level = level,
                promoted = promoted
            )
            FightPropertyFormat(propertyCurveValue.Property, result)
        }
    }

    //计算指定的属性值
    fun calculateFightProperty(
        propertyCurveValue: PropertyCurveValue,
        promoteId: Int,
        level: Int,
        promoted: Boolean,
        maxLevel: Int = 90
    ): Float {
        //通过突破id获取突破成长曲线
        val promoteGrowCurveMap = promoteMap[promoteId]

        //属性基础值乘以相应等级的成长曲线对应值
        val levelPropertyValue =
            propertyCurveValue.Value * (growCurveMap[level]?.Curves
                ?.takeFirstIf { it.Type == propertyCurveValue.Type }?.Value ?: 1f)

        //突破等级
        val promoteLevel = getPromoteLevel(level, maxLevel, promoted)

        //等级突破后额外值
        return levelPropertyValue + (promoteGrowCurveMap?.get(promoteLevel)?.AddProperties
            ?.takeFirstIf { it.Type == propertyCurveValue.Property }?.Value ?: 0f)
    }



    private fun getPromoteLevel(level: Int, maxLevel: Int, promoted: Boolean): Int =
        if (maxLevel <= 70) {
            if (promoted) {
                if (level >= 60) 4
                else if (level >= 50) 3
                else if (level >= 40) 2
                else if (level >= 20) 1
                else 0
            } else {
                if (level > 60) 4
                else if (level > 50) 3
                else if (level > 40) 2
                else if (level > 20) 1
                else 0
            }
        } else {
            if (promoted) {
                if (level >= 80) 6
                else if (level >= 70) 5
                else if (level >= 60) 4
                else if (level >= 50) 3
                else if (level >= 40) 2
                else if (level >= 20) 1
                else 0
            } else {
                if (level > 80) 6
                else if (level > 70) 5
                else if (level > 60) 4
                else if (level > 50) 3
                else if (level > 40) 2
                else if (level > 20) 1
                else 0
            }
        }

}