package com.lianyi.paimonsnotebook.ui.screen.gacha.service

import com.lianyi.paimonsnotebook.common.util.file.FileHelper
import com.lianyi.paimonsnotebook.common.util.json.JSON
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.util.MetadataHelper
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.avatar.AvatarData
import org.json.JSONArray

class GachaLogService {

    private val itemMap = mutableMapOf<String, Int>()

    init {
        val avatarJson = FileHelper.getMetadata(MetadataHelper.FileNameAvatar)?.readText() ?: "[]"
        val weaponJson = FileHelper.getMetadata(MetadataHelper.FileNameWeapon)?.readText() ?: "[]"

        val avatarJsonArray = JSONArray(avatarJson)
        repeat(avatarJsonArray.length()){
            val data = JSON.parse<AvatarData>(avatarJsonArray.getJSONObject(it).toString())
            itemMap[data.name] = data.id
        }

        val weaponJsonArray = JSONArray(weaponJson)
        repeat(weaponJsonArray.length()){
            val data = JSON.parse<AvatarData>(weaponJsonArray.getJSONObject(it).toString())
            itemMap[data.name] = data.id
        }
    }

    fun getItemIdByName(name: String) = itemMap[name] ?: 0

}