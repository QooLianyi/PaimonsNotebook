package com.lianyi.paimonsnotebook.ui.screen.gacha.service

import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.AvatarService
import com.lianyi.paimonsnotebook.common.web.hutao.genshin.common.service.WeaponService
import com.lianyi.paimonsnotebook.ui.screen.gacha.data.GachaModel

class GachaLogService {

    private val itemMap = mutableMapOf<String, GachaModel>()
    private val gachaModelMap = mutableMapOf<Int, GachaModel>()
    private val avatarService = AvatarService(onMissingFile = this::onMissingFile)
    private val weaponService = WeaponService(onMissingFile = this::onMissingFile)

    //判断该service是否可用
    var isAvailable = true
        private set

    init {
        avatarService.avatarList.forEach { data ->
            val model = GachaModel(
                name = data.name,
                id = data.id,
                rank = data.starCount,
                type = "角色"
            )
            itemMap[data.name] = model
            gachaModelMap[data.id] = model
        }

        weaponService.weaponList.forEach { data ->
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

    private fun onMissingFile() {
        isAvailable = false
    }

    fun getModelByName(name: String) = itemMap[name]

    fun getGachaModelByItemId(itemId: String) = gachaModelMap[itemId.toIntOrNull() ?: -1]

}