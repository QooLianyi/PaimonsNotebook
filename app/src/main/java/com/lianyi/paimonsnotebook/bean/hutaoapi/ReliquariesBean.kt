package com.lianyi.paimonsnotebook.bean.hutaoapi

import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.util.getListSP
import com.lianyi.paimonsnotebook.util.sp
import com.lianyi.paimonsnotebook.util.toList
import org.json.JSONArray

class ReliquariesBean(val id: Int,val name: String,val url: String) {
    companion object{
        val reliquariesList by lazy {
            val list = mutableListOf<ReliquariesBean>()
            JSONArray(getListSP(sp, JsonCacheName.HUTAO_RELIQUARIES_LIST)).toList(list)
            list
        }

        val reliquariesMap by lazy {
            val map = mutableMapOf<Int,ReliquariesBean>()
            reliquariesList.forEach {
                map+= it.id to it
            }
            map
        }

    }
}