package com.lianyi.paimonsnotebook.lib.data

import android.content.Intent
import android.net.Uri
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.PaimonsNotebookLatestBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import kotlin.concurrent.thread

/*
* 此类提供更新本地缓存数据方法
*
* */

class UpdateInformation {
    companion object{
        private lateinit var document:Document
        private var getDocumentTime = 0L
        private const val cacheTime = 600000L

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

        fun getPaimonsNotebookWeb(selector:String,block: (String) -> Unit){
            if(System.currentTimeMillis()-getDocumentTime<=cacheTime){
                val text = document.select(selector).text()
                block(text)
            }else{
                thread {
                    document = Jsoup.connect(Constants.PAIMONS_NOTEBOOK_WEB).get()
                    getDocumentTime = System.currentTimeMillis()
                    val text = document.select(selector).text()
                    block(text)
                }
            }
        }

        fun getNewVersionApp(){
            Ok.get(Constants.PAIMONS_NOTE_BOOK_LATEST){
                val latestBean = GSON.fromJson(it.toString(), PaimonsNotebookLatestBean::class.java)
                val uri = Uri.parse(latestBean.assets.first().browser_download_url)
                val intent = Intent(Intent.ACTION_VIEW,uri)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                PaiMonsNoteBook.context.startActivity(intent)
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