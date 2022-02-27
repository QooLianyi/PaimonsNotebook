package com.lianyi.paimonsnotebook.bean.hutaoapi

import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.util.getListSP
import com.lianyi.paimonsnotebook.util.toList
import com.lianyi.paimonsnotebook.util.wsp
import org.json.JSONArray

class WeaponsBean(val id: Int ,val name: String,val url: String) {
    companion object{
        val weaponsList by lazy {
            val list = mutableListOf<WeaponsBean>()
            JSONArray(getListSP(wsp,JsonCacheName.HUTAO_WEAPON_LIST)).toList(list)
            list
        }

        val weaponMap by lazy {
            val map = mutableMapOf<Int,WeaponsBean>()
            weaponsList.forEach {
                map+= it.id to it
            }
            map
        }

        fun getWeaponById(id:Int):WeaponsBean?{
            return weaponMap[id]
        }
    }
}