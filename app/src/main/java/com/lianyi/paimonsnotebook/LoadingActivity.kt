package com.lianyi.paimonsnotebook

import android.content.Intent
import android.os.Bundle
import com.lianyi.paimonsnotebook.ui.activity.HoYoLabLoginActivity
import com.lianyi.paimonsnotebook.base.BaseActivity
import com.lianyi.paimonsnotebook.config.ActivityResponseCode
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.config.JsonCacheName
import com.lianyi.paimonsnotebook.config.Settings
import com.lianyi.paimonsnotebook.ui.RefreshData
import com.lianyi.paimonsnotebook.util.*

class LoadingActivity : BaseActivity() {

    private var initFinished = false
    private var successInitCount = 0
    private var failedInitCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        //判断是否有账号是处于登录状态
        if(mainUser?.isNotLogin()==true){
            goLogin()
        }else{
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

        RefreshData.getBlackBoard {
            if(it){
                successInitCount++
            }else{
                failedInitCount++
            }
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

        if(wsp.getString(JsonCacheName.WEAPON_LIST,"").isNullOrEmpty()||csp.getString(JsonCacheName.CHARACTER_LIST,"").isNullOrEmpty()){
            RefreshData.getJsonData {
                runOnUiThread {
                    if(it){
                        successInitCount++
                    }else{
                        failedInitCount++
                    }
                    checkInitStatus()
                }
            }
        }else{
            successInitCount++
            checkInitStatus()
        }

    }

    //检查初始化状态
    private fun checkInitStatus(){
        runOnUiThread {
            if(successInitCount==AppConfig.APP_INIT_COUNT){
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

}