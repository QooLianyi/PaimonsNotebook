package com.lianyi.paimonsnotebook.ui

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.core.graphics.drawable.IconCompat
import androidx.core.graphics.drawable.toBitmap
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.GetGameRolesByCookieBean
import com.lianyi.paimonsnotebook.bean.WeaponBean
import com.lianyi.paimonsnotebook.card.service.ForegroundTest
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.databinding.ActivityLoadingBinding
import com.lianyi.paimonsnotebook.lib.data.UpdateInformation
import com.lianyi.paimonsnotebook.lib.information.*
import com.lianyi.paimonsnotebook.ui.activity.launch.SetCookieActivity
import com.lianyi.paimonsnotebook.util.*
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
//        test()
    }

    private fun test(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(Intent(this,ForegroundTest::class.java))
        }else{
            startService(Intent(this,ForegroundTest::class.java))
        }

//        WorkManager.getInstance(baseContext).cancelUniqueWork(ResinProgressBar.WORKER_NAME)
//        val repeatRequest = PeriodicWorkRequestBuilder<ResinWorker>(1, TimeUnit.SECONDS)
//            .build()
//        WorkManager.getInstance(baseContext).enqueueUniquePeriodicWork(
//            ResinProgressBar.WORKER_NAME,
//            ExistingPeriodicWorkPolicy.KEEP,repeatRequest)
//
//        val person = Person.Builder()
//            .setIcon(IconCompat.createWithResource(bind.root.context,R.drawable.icon_klee))
//            .setName("用户名称A")
//            .setKey("UserAKey")
//            .build()
//
//        val person1 = Person.Builder()
//            .setIcon(IconCompat.createWithResource(bind.root.context,R.drawable.icon_klee))
//            .setName("用户名称A")
//            .setKey("UserAKey")
//            .build()
//        val person2 = Person.Builder()
//            .setIcon(IconCompat.createWithResource(bind.root.context,R.drawable.icon_klee))
//            .setName("用户名称B")
//            .setKey("UserBKey")
//            .build()
//        val person3 = Person.Builder()
//            .setIcon(IconCompat.createWithResource(bind.root.context,R.drawable.icon_klee))
//            .setName("用户名称C")
//            .setKey("UserCKey")
//            .build()
//        val line = NotificationCompat.MessagingStyle(person).addMessage("发布的内容",System.currentTimeMillis(),person)
//
//
//        val channelId = "notice"
//        val bit = ContextCompat.getDrawable(this,R.drawable.img_home_background)!!.toBitmap()
//        val builder = NotificationCompat.Builder(this,channelId)
//            .setSmallIcon(R.drawable.icon_klee)
//            .setContentTitle("这是一个标题")
//            .setContentText("这是发送的内容")
//            .setStyle(NotificationCompat.MessagingStyle(person)
//                .addMessage("这是一条消息A",System.currentTimeMillis(),person1)
//                .addMessage("这是一条消息B",System.currentTimeMillis(),person2)
//                .addMessage("这是一条消息C",System.currentTimeMillis(),person3)
//            )

//            .setLargeIcon(bit)
//            .setStyle(NotificationCompat.InboxStyle()
//                .addLine("A")
//                .addLine("B")
//                .addLine("C")
//                .addLine("D")
//                .addLine("E")
//                .addLine("F")
//                .addLine("G")
//            )
//            .setStyle(NotificationCompat.BigPictureStyle()
//                .bigPicture(bit))
//            .build()
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(channelId,"name",NotificationManager.IMPORTANCE_DEFAULT)
//            manager.createNotificationChannel(channel)
//        }
//        manager.notify(1,builder)

    }


    //初始化信息
    private fun initInfo(){
        //判断上次启动时版本号和本次版本号是否相同 不同则刷新列表
        if(sp.getString(Constants.LAST_LAUNCH_APP_NAME,"")!=PaiMonsNoteBook.APP_VERSION_NAME){
            UpdateInformation.refreshJSON()
        }

        runOnUiThread {
            if(sp.getBoolean(Constants.SP_NEED_UPDATE,false)){
                showFailureAlertDialog(bind.root.context,getString(R.string.paimonsnotebook_need_update_title),getString(R.string.paimonsnotebook_need_update_context),false)
                thread {
                    Thread.sleep(5000)
                    UpdateInformation.getNewVersionApp()
                }
            }else{
                goA<MainActivity>()
                finish()
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
                    removeMainUser()
                    runOnUiThread {
                        "Cookie失效,请重新设置Cookie".showLong()
                    }
                }
            }
        }
    }

    //移除usp缓存的默认账号
    private fun removeMainUser(){
        usp.edit {
            remove(JsonCacheName.MAIN_USER_NAME)
            apply()
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
        val intent = Intent(this, SetCookieActivity::class.java)
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