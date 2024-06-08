package com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.Material
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.item.MaterialType
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
        /*
        * 过滤的类型
        * 除此之外的类型都不需要
        * */
        val typeFilter = setOf(
            MaterialType.MATERIAL_AVATAR_MATERIAL, //角色与武器的突破材料
            MaterialType.MATERIAL_ELEM_CRYSTAL, //角色突破所需的元素水晶
            MaterialType.MATERIAL_EXCHANGE, //地区特产材料
            MaterialType.MATERIAL_WEAPON_EXP_STONE, //武器突破材料
            MaterialType.MATERIAL_EXP_FRUIT, //角色经验
            MaterialType.MATERIAL_ADSORBATE //货币
        )

        repeat(jsonArray.length()) {
            val jsonString = jsonArray.getJSONObject(it).toString()
            val data = JSON.parse<Material>(jsonString)

            if(typeFilter.contains(data.MaterialType)){
                materialMap[data.Id] = data
            }
        }
    }

    fun getMaterialListByIds(ids: List<Int>): List<Material> {
        val list = mutableListOf<Material>()

        ids.forEach { id ->
            list += materialMap[id] ?: defaultMaterial
        }

        return list
    }

    fun getMaterialById(id: Int) = materialMap[id] ?: defaultMaterial
}