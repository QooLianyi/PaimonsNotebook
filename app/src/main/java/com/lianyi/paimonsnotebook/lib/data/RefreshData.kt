package com.lianyi.paimonsnotebook.lib.data

import android.util.Log
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
            Ok.get(MiHoYoApi.HOME_BANNER){
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
            Ok.get(MiHoYoApi.BLACK_BOARD){
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
                    val document = Jsoup.connect(MiHoYoApi.JSON_DATA).get()
                    val weaponJsonSelect = "span.weapon li"
                    val characterJsonSelect = "span.character li"

                    val jsonArray = JSONArray(document.select(characterJsonSelect).text())
                    val characterData = mutableListOf<CharacterBean>()
                    repeat(jsonArray.length()-1){
                        characterData += GSON.fromJson(jsonArray[it].toString(), CharacterBean::class.java)
                    }

                    //角色获取
                    //控制台打印
                    characterData.forEach {
                        println(
                            """
                        名称:${it.name}
                        src:${it.icon}
                        元素类型:${Element.getNameByType(it.element)}
                        星级:${it.star}
                        装备类型:${WeaponType.getNameByType(it.weaponType)}
                        主属性:${it.upAttribute}
                        副属性:${it.upAttribute}
                    """.trimIndent()
                        )
                        println("------------------------------------------------------------------------------")
                    }

                    val cEdit = csp.edit()
                    cEdit.putString(JsonCacheName.CHARACTER_LIST,GSON.toJson(characterData))
                    cEdit.apply()


                    //武器获取
                    val weaponJson = JSONArray(document.select(weaponJsonSelect).text())
                    val weaponData = mutableListOf<WeaponBean>()
                    repeat(weaponJson.length()-1){
                        weaponData += GSON.fromJson(weaponJson[it].toString(),WeaponBean::class.java)
                    }
                    //控制台打印
                    weaponData.forEach {
                        println(
                            """
                        名称:${it.name}
                        src:${it.icon}
                        武器类型:${WeaponType.getNameByType(it.weaponType)}
                        星级:${it.star}
                        主属性:${it.attributeName}
                        主属性值:${it.attributeNameValue}
                        效果名称:${it.effectName}
                        效果:${it.effect}
                    """.trimIndent()
                        )
                        println("------------------------------------------------------------------------------")
                    }
                    println(weaponData.size)

                    val wEdit = wsp.edit()
                    wEdit.putString(JsonCacheName.WEAPON_LIST, GSON.toJson(weaponData))
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
            Ok.get(MiHoYoApi.getDailyNoteUrl(gameUid,server)){
                if(it.ok){
                    edit.putString(Constants.SP_DAILY_NOTE_NAME+gameUid,it.optString("data"))
                    edit.apply()
                }
                block(it.ok)
            }
        }

        fun getMonthLedger(month:String,gameUID: String,server: String,block: (Boolean) -> Unit){
            val edit = sp.edit()
            Ok.get(MiHoYoApi.getMonthInfoUrl(month, gameUID, server)){
                if(it.ok){
                    edit.putString(Constants.SP_MONTH_LEDGER_NAME+ mainUser?.gameUid,it.optString("data"))
                    edit.apply()
                }
                block(it.ok)
            }
        }

    }
}