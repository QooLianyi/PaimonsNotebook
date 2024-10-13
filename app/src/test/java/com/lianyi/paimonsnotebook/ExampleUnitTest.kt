package com.lianyi.paimonsnotebook

import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.util.parameter.getParameterizedType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.intrinsic.ReliquaryType
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquaryData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.reliquary.ReliquarySetData
import kotlinx.coroutines.delay
import org.json.JSONObject
import org.junit.Test
import java.io.File
import java.nio.charset.Charset

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun codeTest(): Unit {

        JSONObject().apply {
            put("ee","ww")
            put("ext",JSONObject(buildMap {
                put("a","1")
                put("c","2")
                put("b","3")
            }).toString())
        }.apply {
            println(this.toString())
        }

    }


    private suspend fun delayCall(i: Int) {
        val r = (1..10).random()
        println("i = ${i} r = ${r} start")
        delay(r * 1000L)
        println("i = ${i} r = ${r} end")
    }


    /*

    * */
    @Test
    fun test() {
        val reliquaryFile =
            File("D:\\Project\\PaimonsNotebook\\project\\PaimonsNotebook\\app\\src\\test\\java\\com\\lianyi\\paimonsnotebook\\Reliquary.json")
        val reliquarySetFile =
            File("D:\\Project\\PaimonsNotebook\\project\\PaimonsNotebook\\app\\src\\test\\java\\com\\lianyi\\paimonsnotebook\\ReliquarySet.json")

        val list = JSON.parse<List<ReliquaryData>>(
            reliquaryFile.readText(),
            getParameterizedType(List::class.java, ReliquaryData::class.java)
        )
        val setList = JSON.parse<List<ReliquarySetData>>(
            reliquarySetFile.readText(),
            getParameterizedType(List::class.java, ReliquarySetData::class.java)
        )

        val map = list.groupBy { it.SetId }.mapValues { entry ->
            //只保留最高等级的圣遗物
            val maxRankLevel = entry.value.maxOf { it.RankLevel }
            entry.value.filter { it.RankLevel == maxRankLevel }.sortedBy { it.EquipType }
        }

        val setMap = setList.associateBy { it.SetId }

        map.forEach { (t, u) ->
            //圣遗物套装效果
            val setData = setMap[t]
            if (setData == null) {
                println("null -- ${t} ${u}")
                return@forEach
            }

            println("${t} ${u}")

            println("${setData.Name} - ${setData.SetId}")

            setData.NeedNumber.forEachIndexed { index, i ->
                if (index < setData.Descriptions.size) {
                    println("${i}件套效果 : ${setData.Descriptions[index]}")
                } else {
                    println("index = ${index} 圣遗物效果索引溢出")
                }
            }

            u.forEach {
                println("${it.Name} - ${ReliquaryType.getReliquaryNameByType(it.EquipType)} - ${it.RankLevel}")
            }
        }
    }
}