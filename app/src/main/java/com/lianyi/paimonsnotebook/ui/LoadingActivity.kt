package com.lianyi.paimonsnotebook.ui

import android.content.Intent
import android.os.Bundle
import androidx.core.content.edit
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.GetGameRolesByCookieBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.databinding.ActivityLoadingBinding
import com.lianyi.paimonsnotebook.lib.data.UpdateInformation
import com.lianyi.paimonsnotebook.lib.information.*
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

        setViewMarginBottomByNavigationBarHeight(bind.setCookie)
        //判断是否有账号是处于登录状态
        checkCookie()
        test()
    }

    private fun test(){
//        WorkManager.getInstance(baseContext).cancelUniqueWork(ResinProgressBar.WORKER_NAME)
//        val repeatRequest = PeriodicWorkRequestBuilder<ResinWorker>(1, TimeUnit.SECONDS)
//            .build()
//        WorkManager.getInstance(baseContext).enqueueUniquePeriodicWork(
//            ResinProgressBar.WORKER_NAME,
//            ExistingPeriodicWorkPolicy.KEEP,repeatRequest)
    }


    //初始化信息
    private fun initInfo(){
        //判断上次启动时版本号和本次版本号是否相同 不同则刷新列表
        if(sp.getString(Constants.LAST_LAUNCH_APP_NAME,"")!=PaiMonsNoteBook.APP_VERSION_NAME){
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
            sp.edit {
                putString(Constants.LAST_LAUNCH_APP_NAME,PaiMonsNoteBook.APP_VERSION_NAME)
                apply()
            }
        }

        runOnUiThread {
            if(sp.getBoolean(Constants.SP_NEED_UPDATE,false)){
                showFailureAlertDialog(bind.root.context,getString(R.string.paimonsnotebook_need_update_title),getString(R.string.paimonsnotebook_need_update_context),false)
                thread {
                    Thread.sleep(5000)
                    UpdateInformation.getNewVersionApp()
                }
            }else{
//                if(sp.getBoolean("ena",true)){
                    thread {
                        goA<MainActivity>()
                        Thread.sleep(500)
                        finish()
                    }
//                }else{
//                    showFailureAlertDialog(bind.root.context,getString(R.string.paimonsnotebook_not_support_title),getString(R.string.paimonsnotebook_not_support_context),false)
//                    thread {
//                        Thread.sleep(5000)
//                        error(getString(R.string.paimonsnotebook_not_support_title))
//                    }
//                }
            }
        }
    }

    private fun checkCookie(){
        if(mainUser?.isNull()==true){
            showAddCookieButton()
        }else{
            SetCookieActivity.checkCookie(mainUser!!.loginUid, mainUser!!.lToken, mainUser!!.cookieToken){ b: Boolean, roles: GetGameRolesByCookieBean? ->
                if(b){
                    //更新游戏等级
                    with(mainUser!!){
                        gameLevel = roles!!.list.first().level
                    }
                    SetCookieActivity.refreshMainUserInformation()

                    initInfo()
                    initFinished = true
                }else{
                    showAddCookieButton()
                    runOnUiThread {
                        "Cookie失效,请重新设置Cookie".showLong()
                    }
                }
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