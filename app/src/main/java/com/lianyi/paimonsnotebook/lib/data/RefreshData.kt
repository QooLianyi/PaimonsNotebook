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

        //获取 全部角色、全部武器 所需的数据
        fun getJsonData(block: (Boolean) -> Unit){
            thread {
                try {
                    val document = Jsoup.connect(MiHoYoApi.JSON_DATA).get()
                    val weaponJsonSelect = "span.weapon li"
                    val characterJsonSelect = "span.character li"

                    //角色获取
                    try {
                        val jsonArray = JSONArray(document.select(characterJsonSelect).text())
                        val characterData = mutableListOf<CharacterBean>()
                        repeat(jsonArray.length()-1){
                            characterData += GSON.fromJson(jsonArray[it].toString(), CharacterBean::class.java)
                        }
                        val cEdit = csp.edit()
                        cEdit.putString(JsonCacheName.CHARACTER_LIST,GSON.toJson(characterData))
                        cEdit.apply()
                    }catch (e:Exception){
                        loadingWindowDismiss()
                        e.printStackTrace()
                        block(false)
                    }

                    //武器获取
                    try{
                        val weaponJson = JSONArray(document.select(weaponJsonSelect).text())
                        val weaponData = mutableListOf<WeaponBean>()
                        repeat(weaponJson.length()-1){
                            weaponData += GSON.fromJson(weaponJson[it].toString(),WeaponBean::class.java)
                        }

                        val wEdit = wsp.edit()
                        wEdit.putString(JsonCacheName.WEAPON_LIST, GSON.toJson(weaponData))
                        wEdit.apply()
                        block(true)
                    }catch (e:Exception){
                        loadingWindowDismiss()
                        e.printStackTrace()
                        block(false)
                    }
                }catch (e:Exception){
                    block(false)
                }
            }
        }

        //更新便笺
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

        //更新旅行者手记
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