package com.lianyi.paimonsnotebook.lib.data

import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import org.jsoup.Jsoup
import java.lang.Exception
import kotlin.concurrent.thread

/*
* 此类提供刷新本地缓存数据方法
*
* */

class RefreshData {
    companion object{

        //获得banner 主页公告
        fun getBanner(block:(Boolean)->Unit){
            Ok.get(MiHoYoApi.OFFICIAL_RECOMMEND_POST){
                if(it.ok){
                    val edit = sp.edit()
                    edit.putString(JsonCacheName.BANNER_CACHE,it.toString())
                    edit.apply()
                }
                block(it.ok)
            }
        }

        //获取 全部角色、全部武器 所需的数据
        fun getJsonData(block: (Boolean,count:Int) -> Unit){
            thread {
                try {
                    val document = Jsoup.connect(MiHoYoApi.JSON_DATA).get()
                    val weaponJsonSelect = "span.weapon li"
                    val characterJsonSelect = "span.character li"

                    var newDataCount = 0

                    //角色获取
                    try {
                        val characterData = mutableListOf<CharacterBean>()
                        JSONArray(document.select(characterJsonSelect).text()).toListUntil(characterData)
                        csp.edit().apply {
                           putString(JsonCacheName.CHARACTER_LIST,GSON.toJson(characterData))
                           apply()
                        }
                        newDataCount += characterData.size - CharacterBean.characterList.size
                    }catch (e:Exception){
                        dismissLoadingWindow()
                        e.printStackTrace()
                        block(false,0)
                    }

                    //武器获取
                    try{
                        val weaponData = mutableListOf<WeaponBean>()
                        JSONArray(document.select(weaponJsonSelect).text()).toListUntil(weaponData)
                        wsp.edit().apply {
                            putString(JsonCacheName.WEAPON_LIST, GSON.toJson(weaponData))
                            apply()
                        }
                        newDataCount += weaponData.size - WeaponBean.weaponList.size
                        block(true,newDataCount)
                    }catch (e:Exception){
                        dismissLoadingWindow()
                        e.printStackTrace()
                        block(false,0)
                    }
                }catch (e:Exception){
                    block(false,0)
                }
            }
        }
    }
}