package com.lianyi.paimonsnotebook.ui.screen.gacha.service

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.weapon.WeaponData
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.GachaModel
import org.json.JSONArray

class GachaLogService {

    private val itemMap = mutableMapOf<String, GachaModel>()
    private val gachaModelMap = mutableMapOf<Int, GachaModel>()


    init {
        val avatarJson = FileHelper.getMetadata(MetadataHelper.FileNameAvatar)?.readText() ?: "[]"
        val weaponJson = FileHelper.getMetadata(MetadataHelper.FileNameWeapon)?.readText() ?: "[]"

        val avatarJsonArray = JSONArray(avatarJson)
        repeat(avatarJsonArray.length()) {
            val data = JSON.parse<AvatarData>(avatarJsonArray.getJSONObject(it).toString())
            val model = GachaModel(
                name = data.name,
                id = data.id,
                rank = data.starCount,
                type = "角色"
            )
            itemMap[data.name] = model
            gachaModelMap[data.id] = model
        }

        val weaponJsonArray = JSONArray(weaponJson)
        repeat(weaponJsonArray.length()) {
            val data = JSON.parse<WeaponData>(weaponJsonArray.getJSONObject(it).toString())
            val model = GachaModel(
                name = data.name,
                id = data.id,
                rank = data.rankLevel,
                type = "武器"
            )
            itemMap[data.name] = model
            gachaModelMap[data.id] = model
        }
    }

    fun getModelByName(name: String) = itemMap[name]

    fun getGachaModelByItemId(itemId: String) = gachaModelMap[itemId.toIntOrNull() ?: -1]

}