package com.lianyi.paimonsnotebook.lib

import com.lianyi.paimonsnotebook.bean.BlackBoardBean
import com.lianyi.paimonsnotebook.bean.EntityBean
import com.lianyi.paimonsnotebook.config.EntityType
import com.lianyi.paimonsnotebook.config.JsonCacheName
import com.lianyi.paimonsnotebook.util.GSON
import com.lianyi.paimonsnotebook.util.csp
import com.lianyi.paimonsnotebook.util.sp
import com.lianyi.paimonsnotebook.util.wsp

class MetaData {
    companion object{
        //初始化本地数据
        fun loadDataList(block:(result:Boolean,count:Int)->Unit){
            val blackBoard = GSON.fromJson(sp.getString(JsonCacheName.BLACK_BOARD,"")!!,BlackBoardBean::class.java)
            val character = mutableListOf<EntityBean>()
            val weapon = mutableListOf<EntityBean>()
            blackBoard.data.list.forEach {
                if(it.kind=="2"){
                    when(it.break_type){
                        "1"->weapon += EntityBean(it.title,it.img_url,EntityType.CHARACTER,it.content_id)
                        "2" -> character += EntityBean(it.title,it.img_url,EntityType.CHARACTER,it.content_id)
                    }
                }
            }

            val cEdit = csp.edit()
            val wEdit = wsp.edit()
            cEdit.putString(JsonCacheName.CHARACTER_LIST, GSON.toJson(character))
            wEdit.putString(JsonCacheName.WEAPON_LIST,GSON.toJson(weapon))
            cEdit.apply()
            wEdit.apply()

        }
    }

    private fun loadWeapon(block:(result:Boolean,count:Int)->Unit){

    }

}