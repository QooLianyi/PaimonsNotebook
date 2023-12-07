package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import org.json.JSONArray

class MaterialService(onMissingFile: () -> Unit) {

    private val materialMap = mutableMapOf<Int, Material>()

    private val defaultMaterial by lazy {
        Material(
            Description = "这是一个空物品",
            EffectDescription = null,
            Icon = "UI_ItemIcon_100001",
            Id = 0,
            ItemType = 0,
            MaterialType = 0,
            Name = "空物品",
            RankLevel = 0,
            TypeDescription = ""
        )
    }

    init {
        val materialFile = FileHelper.getMetadata(MetadataHelper.FileNameMaterial)

        if (materialFile == null) {
            onMissingFile.invoke()
        } else {
            setMaterialMap(JSONArray(materialFile.readText()))
        }
    }

    private fun setMaterialMap(jsonArray: JSONArray) {
        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            val data = JSON.parse<Material>(jsonString)
            materialMap[data.Id] = data
        }
    }

    fun getMaterialListByIds(ids: List<Int>): List<Material> {
        val list = mutableListOf<Material>()

        ids.forEach { id ->
            list += materialMap[id] ?: defaultMaterial
        }

        return list
    }
}