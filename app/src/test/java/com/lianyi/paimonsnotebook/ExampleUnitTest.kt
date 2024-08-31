package com.lianyi.paimonsnotebook

import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.conveter.RelicIconConverter
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ReliquaryType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquaryData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquarySetData
import org.junit.Test
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    /*

    * */
    @Test
    fun test() {
        val reliquaryFile = File("D:\\Project\\PaimonsNotebook\\project\\PaimonsNotebook\\app\\src\\test\\java\\com\\lianyi\\paimonsnotebook\\Reliquary.json")
        val reliquarySetFile = File("D:\\Project\\PaimonsNotebook\\project\\PaimonsNotebook\\app\\src\\test\\java\\com\\lianyi\\paimonsnotebook\\ReliquarySet.json")

        val list = JSON.parse<List<ReliquaryData>>(reliquaryFile.readText(), getParameterizedType(List::class.java,ReliquaryData::class.java))
        val setList = JSON.parse<List<ReliquarySetData>>(reliquarySetFile.readText(), getParameterizedType(List::class.java,ReliquarySetData::class.java))

        val map = list.groupBy { it.SetId }.mapValues { entry ->
            //只保留最高等级的圣遗物
            val maxRankLevel = entry.value.maxOf { it.RankLevel }
            entry.value.filter { it.RankLevel == maxRankLevel }.sortedBy { it.EquipType }
        }

        val setMap = setList.associateBy { it.SetId }

        map.forEach { (t, u) ->
            //圣遗物套装效果
            val setData = setMap[t]
            if(setData == null){
                println("null -- ${t} ${u}")
                return@forEach
            }

            println("${setData.Name} - ${setData.SetId}")

            setData.NeedNumber.forEachIndexed { index, i ->
                if(index < setData.Descriptions.size){
                    println("${i}件套效果 : ${setData.Descriptions[index]}")
                }else{
                    println("index = ${index} 圣遗物效果索引溢出")
                }
            }

            u.forEach {
                println("${it.Name} - ${ReliquaryType.getReliquaryNameByType(it.EquipType)} - ${it.RankLevel}")
            }
        }
    }
}