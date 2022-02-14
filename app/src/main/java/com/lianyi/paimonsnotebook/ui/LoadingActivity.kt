package com.lianyi.paimonsnotebook.ui

import android.content.Intent
import android.os.Bundle
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.bean.account.UserBean
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.databinding.ActivityLoadingBinding
import com.lianyi.paimonsnotebook.lib.information.ActivityRequestCode
import com.lianyi.paimonsnotebook.lib.information.ActivityResponseCode
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.ui.activity.SetCookieActivity
import com.lianyi.paimonsnotebook.util.*
import org.json.JSONArray
import java.nio.charset.Charset
import kotlin.concurrent.thread

class LoadingActivity : BaseActivity() {

    private var initFinished = false
    lateinit var bind: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoadingBinding.inflate(layoutInflater)

        setContentView(bind.root)

        //判断是否有账号是处于登录状态
        checkCookie()
    }

    //初始化信息
    private fun initInfo(){
        if(wsp.getString(JsonCacheName.WEAPON_LIST,"").isNullOrEmpty()||csp.getString(JsonCacheName.CHARACTER_LIST,"").isNullOrEmpty()){
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
        }

        thread {
            goA<MainActivity>()
            Thread.sleep(500)
            finish()
        }
    }

    private fun checkCookie(){
        if(mainUser?.isNull()==true){
            showAddCookieButton()
        }else{
            thread {
                var pass = false
                Ok.getSync(MiHoYoApi.getAccountInformation(mainUser!!.loginUid), mainUser!!){
                    pass = it.ok
                }
                if(pass){
                    initInfo()
                    initFinished = true
                    return@thread
                }
                showAddCookieButton()
            }
        }
    }

    private fun showAddCookieButton(){
        runOnUiThread {
            bind.setCookie.show()
            bind.setCookie.animate().alpha(1f).setDuration(1000).start()
            bind.setCookie.setOnClickListener {
                goLogin()
            }
        }
    }

    //前往登录
    private fun goLogin(){
        val intent = Intent(this,SetCookieActivity::class.java)
        startActivityForResult(intent,ActivityRequestCode.SET_COOKIE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== ActivityResponseCode.OK){
            when(requestCode){
                ActivityRequestCode.SET_COOKIE->{
                    bind.setCookie.gone()
                    checkCookie()
                }
            }
        }
    }
}