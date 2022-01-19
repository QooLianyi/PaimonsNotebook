package com.lianyi.paimonsnotebook.ui

import android.content.Intent
import android.os.Bundle
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.ui.activity.HoYoLabLoginActivity
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.lib.data.RefreshData
import com.lianyi.paimonsnotebook.lib.information.ActivityResponseCode
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import java.nio.charset.Charset

class LoadingActivity : BaseActivity() {

    private var initFinished = false
    private var successInitCount = 0
    private var failedInitCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_loading)
        super.onCreate(savedInstanceState)

        //判断是否有账号是处于登录状态
        if (mainUser?.isNotLogin() == true) {
            goLogin()
        } else {
            initInfo()
        }
    }

    //初始化信息
    private fun initInfo(){
        //更新便笺
        RefreshData.getDailyNote(mainUser!!.gameUid, mainUser!!.region){
            if(it){
                successInitCount++
            }else{
                failedInitCount++
            }
            checkInitStatus()
        }

//        RefreshData.getBlackBoard {
//            if(it){
//                successInitCount++
//            }else{
//                failedInitCount++
//            }
//            checkInitStatus()
//        }

        if(sp.getString(JsonCacheName.WEAPON_LIST,"").isNullOrEmpty()||sp.getString(JsonCacheName.CHARACTER_LIST,"").isNullOrEmpty()){
//            RefreshData.getJsonData {
//                if(it){
//                    successInitCount++
//                }else{
//                    failedInitCount++
//                }
//                checkInitStatus()
//            }
            val characterText = resources.assets.open("CharacterList")
            val characterBuff =ByteArray(characterText.available())
            characterText.read(characterBuff)
            characterText.close()
            val characterList = mutableListOf<CharacterBean>()
            JSONArray(String(characterBuff, Charset.defaultCharset())).toList(characterList)
            csp.edit().apply{
                putString(JsonCacheName.CHARACTER_LIST,GSON.toJson(characterList))
                apply()
            }

            val weaponText = resources.assets.open("WeaponList")
            val weaponBuff =ByteArray(weaponText.available())
            weaponText.read(weaponBuff)
            weaponText.close()
            val weaponList = mutableListOf<WeaponBean>()
            JSONArray(String(weaponBuff, Charset.defaultCharset())).toList(weaponList)
            wsp.edit().apply{
                putString(JsonCacheName.WEAPON_LIST,GSON.toJson(weaponList))
                apply()
            }
            successInitCount++
            checkInitStatus()
        }else{
            successInitCount++
            checkInitStatus()
        }

        RefreshData.getBanner {
            if(it){
                successInitCount++
            }else{
                failedInitCount++
            }
            checkInitStatus()
        }

    }

    //检查初始化状态
    private fun checkInitStatus(){
        runOnUiThread {
            if(successInitCount==AppConfig.APP_INIT_COUNT){
                initFinished = true
                goA<MainActivity>()
            }else if(successInitCount+failedInitCount==AppConfig.APP_INIT_COUNT){
                if(mainUser?.isNotLogin() == true){
                    "登录失效,请重新登录".show()
                    val intent = Intent(this,HoYoLabLoginActivity::class.java)
                    startActivityForResult(intent,0)
                }else{
                    "网络异常,请稍后再尝试吧~".show()
                }
            }
        }
    }

    //前往登录
    private fun goLogin(){
        val intent = Intent(this,HoYoLabLoginActivity::class.java)
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            ActivityResponseCode.OK->{
                initInfo()
            }
            else ->{
                runOnUiThread {
                    "请先进行第一次登录".show()
                    goLogin()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if(initFinished){
            finish()
        }
    }

    override fun onBackPressed() {
        if(initFinished){
            finish()
        }
    }

}