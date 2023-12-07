package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.monster.MonsterData
import org.json.JSONArray

/*
* 怪物服务
* */
class MonsterService(onMissingFile: () -> Unit) {
    //怪物列表
    var monsterList = listOf<MonsterData>()
        private set

    init {
        val monsterFile = FileHelper.getMetadata(MetadataHelper.FileNameMonster)

        if (monsterFile != null) {
            setMonsterList(JSONArray(monsterFile.readText()))
        } else {
            onMissingFile.invoke()
        }
    }

    private fun setMonsterList(jsonArray: JSONArray) {
        val list = mutableListOf<MonsterData>()
        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            list += JSON.parse<MonsterData>(jsonString)
        }

        list.sortBy { it.id }

        monsterList = list
    }
}