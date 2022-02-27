package com.lianyi.paimonsnotebook.bean.hutaoapi

import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.util.csp
import com.lianyi.paimonsnotebook.util.getListSP
import com.lianyi.paimonsnotebook.util.toList
import org.json.JSONArray

class AvatarsBean(val id: Int,val name: String,val url: String) {
    companion object{
        val avatarsList by lazy {
            val list = mutableListOf<AvatarsBean>()
            JSONArray(getListSP(csp, JsonCacheName.HUTAO_AVATARS_LIST)).toList(list)
            list
        }

        val avatarsMap by lazy {
            val map = mutableMapOf<Int,AvatarsBean>()
            avatarsList.forEach {
                map+= it.id to it
            }
            map
        }

        fun getAvatarsById(id: Int):AvatarsBean?{
            return avatarsMap[id]
        }

    }
}