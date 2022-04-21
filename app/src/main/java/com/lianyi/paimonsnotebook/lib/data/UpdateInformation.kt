package com.lianyi.paimonsnotebook.lib.data

import android.content.Intent
import android.net.Uri
import androidx.core.content.edit
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.PaimonsNotebookLatestBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.bean.dailynote.DailyNoteBean
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.lang.Exception
import java.nio.charset.Charset
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
                    try {
                        document = Jsoup.connect(Constants.PAIMONS_NOTEBOOK_WEB).get()
                        getDocumentTime = System.currentTimeMillis()
                        val text = document.select(selector).text()
                        block(text)
                    }catch (e:Exception){
                        e.printStackTrace()
                        println("getPaimonsNotebookWeb:连接超时")
                    }
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

        //刷新本地json
        fun refreshJSON(){
            //            "正在进行JSON数据同步".show()
//            showLoading(bind.root.context)
//            UpdateInformation.updateJsonData { b, count ->
//                runOnUiThread {
//                    if(b){
//                        if(count>0){
//                            "同步成功,新增${count}条数据"
//                        }else{
//                            "同步成功,没有发现新数据"
//                        }.show()
//                    }else{
//                        "同步失败啦,请稍后再试吧!\n程序将于5秒后自动退出".showLong()
//                    }
//                    dismissLoadingWindow()
//                }
//            }

            val characterText = PaiMonsNoteBook.context.resources.assets.open("CharacterList")
            val characterBuff =ByteArray(characterText.available())
            characterText.read(characterBuff)
            characterText.close()
            val characterList = mutableListOf<CharacterBean>()
            JSONArray(String(characterBuff, Charset.defaultCharset())).toList(characterList)
            csp.edit().apply{
                putString(JsonCacheName.CHARACTER_LIST,GSON.toJson(characterList))
                apply()
            }

            val weaponText = PaiMonsNoteBook.context.resources.assets.open("WeaponList")
            val weaponBuff =ByteArray(weaponText.available())
            weaponText.read(weaponBuff)
            weaponText.close()
            val weaponList = mutableListOf<WeaponBean>()
            JSONArray(String(weaponBuff, Charset.defaultCharset())).toList(weaponList)
            wsp.edit().apply{
                putString(JsonCacheName.WEAPON_LIST,GSON.toJson(weaponList))
                apply()
            }
            sp.edit {
                putString(Constants.LAST_LAUNCH_APP_NAME,PaiMonsNoteBook.APP_VERSION_NAME)
                apply()
            }
        }

        //覆盖刷新 全部角色、全部武器 所需的数据 返回结果与新增数量
        fun updateJsonData(block: (Boolean, count:Int) -> Unit){
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
                        newDataCount += characterData.size - CharacterBean.characterList.size
                        csp.edit().apply {
                           putString(JsonCacheName.CHARACTER_LIST,GSON.toJson(characterData))
                           apply()
                        }
                    }catch (e:Exception){
                        dismissLoadingWindow()
                        e.printStackTrace()
                        block(false,0)
                    }

                    //武器获取
                    try{
                        val weaponData = mutableListOf<WeaponBean>()
                        JSONArray(document.select(weaponJsonSelect).text()).toListUntil(weaponData)
                        newDataCount += weaponData.size - WeaponBean.weaponList.size
                        wsp.edit().apply {
                            putString(JsonCacheName.WEAPON_LIST, GSON.toJson(weaponData))
                            apply()
                        }
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

        //获得树脂信息
        fun getDailyNote(block: (Boolean,DailyNoteBean?) -> Unit){
            Ok.get(MiHoYoApi.getDailyNoteUrl(mainUser!!.gameUid, mainUser!!.region)){
                var dailyNoteBean:DailyNoteBean? = null
                if(it.ok){
                   dailyNoteBean = GSON.fromJson(it.optString("data"),DailyNoteBean::class.java)
                }
                block(it.ok,dailyNoteBean)
            }
        }

    }
}