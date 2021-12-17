package com.lianyi.paimonsnotebook.ui

import android.util.Log
import com.lianyi.paimonsnotebook.bean.DailyNoteBean
import com.lianyi.paimonsnotebook.bean.EntityJsonBean
import com.lianyi.paimonsnotebook.config.*
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import org.jsoup.Jsoup
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.math.ceil
import kotlin.math.log

/*
* 此类提供刷新本地缓存数据方法
*
* */

class RefreshData {
    companion object{

        //获得banner 主页公告
        fun getBanner(block:(Boolean)->Unit){
            Ok.get(URL.HOME_BANNER){
                if(it.ok){
                    val edit = sp.edit()
                    edit.putString(JsonCacheName.BANNER_CACHE,it.toString())
                    edit.apply()
                }
                block(it.ok)
            }
        }

        //用于主页活动 和每日材料
        fun getBlackBoard(block: (Boolean) -> Unit){
            Ok.get(URL.BLACK_BOARD){
                if(it.ok){
                    val edit = sp.edit()
                    edit.putString(JsonCacheName.BLACK_BOARD,it.toString())
                    edit.apply()
                }
                block(it.ok)
            }
        }

        //获取 全部角色、全部武器 所需的JSON数据
        fun getJsonData(block: (Boolean) -> Unit){
            try {
                thread {
                    println("开始获取")
                    val document = Jsoup.connect(URL.JSON_DATA).get()
                    val weaponJsonSelect = "span.weapon li"
                    val characterJsonSelect = "span.character li"

                    val jsonArray = JSONArray(document.select(characterJsonSelect).text())
                    val characterData = mutableListOf<EntityJsonBean>()
                    repeat(jsonArray.length()-1){
                        characterData += GSON.fromJson(jsonArray[it].toString(), EntityJsonBean::class.java)
                    }

                    //控制台打印
                    characterData.forEach {
                        println(
                            """
                        名称:${it.entity.name}
                        src:${it.entity.iconUrl}
                        元素类型:${CharacterProperty.getNameByType(it.entityType)}
                        星级:${it.star}
                        装备类型:${WeaponType.getNameByType(it.equipType)}
                        主属性:${it.mainProperty}
                        副属性:
                    """.trimIndent()
                        )
                        it.propertyList.forEach {
                            println(it)
                        }
                        println("------------------------------------------------------------------------------")
                    }
                    println(characterData.size)

                    val weaponJson = JSONArray(document.select(weaponJsonSelect).text())
                    val weaponData = mutableListOf<EntityJsonBean>()
                    repeat(weaponJson.length()-1){
                        weaponData += GSON.fromJson(weaponJson[it].toString(),EntityJsonBean::class.java)
                    }

                    //控制台打印
                    weaponData.forEach {
                        println(
                            """
                        名称:${it.entity.name}
                        src:${it.entity.iconUrl}
                        武器类型:${WeaponType.getNameByType(it.entityType)}
                        星级:${it.star}
                        主属性:${it.mainProperty}
                        副属性:${it.propertyList.first()}
                        效果名称:${it.effectName}
                        效果:${it.effect}
                    """.trimIndent()
                        )
                        println("------------------------------------------------------------------------------")

                    }
                    println(weaponData.size)

                    val cEdit = csp.edit()
                    val wEdit = wsp.edit()
                    cEdit.putString(JsonCacheName.CHARACTER_LIST,GSON.toJson(characterData))
                    wEdit.putString(JsonCacheName.WEAPON_LIST, GSON.toJson(weaponData))
                    cEdit.apply()
                    wEdit.apply()
                    block(true)
                }
            }catch (e:Exception){
                Log.e("Lian::", "initJsonData: 超时")
                block(false)
            }
        }

        fun getDailyNote(gameUid:String,server:String,block: (Boolean) -> Unit){
            val edit = sp.edit()
            Ok.get(URL.getDailyNoteUrl(gameUid,server)){
                if(it.ok){
                    edit.putString(Settings.SP_DAILY_NOTE_NAME+gameUid,it.optString("data"))
                    edit.apply()
                }
                block(it.ok)
            }
        }

        fun getMonthLedger(month:String,gameUID: String,server: String,block: (Boolean) -> Unit){
            val edit = sp.edit()
            Ok.get(URL.getMonthInfo(month, gameUID, server)){
                if(it.ok){
                    edit.putString(Settings.SP_MONTH_LEDGER_NAME+ mainUser?.gameUid,it.optString("data"))
                    edit.apply()
                }
                block(it.ok)
            }
        }

    }
}