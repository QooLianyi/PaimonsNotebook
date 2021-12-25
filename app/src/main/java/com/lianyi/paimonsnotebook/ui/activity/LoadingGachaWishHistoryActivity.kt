package com.lianyi.paimonsnotebook.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.GachaLogBean
import com.lianyi.paimonsnotebook.config.AppConfig
import com.lianyi.paimonsnotebook.databinding.ActivityLoadingGachaWishHistoryBinding
import com.lianyi.paimonsnotebook.lib.information.ActivityResponseCode
import com.lianyi.paimonsnotebook.lib.information.GachaType
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.lib.listener.AnimatorFinished
import com.lianyi.paimonsnotebook.util.*
import kotlin.concurrent.thread

class LoadingGachaWishHistoryActivity : BaseActivity() {

    companion object{
        var logUrl = ""
    }

    lateinit var bind:ActivityLoadingGachaWishHistoryBinding
    var isLoading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityLoadingGachaWishHistoryBinding.inflate(layoutInflater)
        setContentView(bind.root)

        setLoadingAnimation()
        loadGachaHistory()
    }

    private fun loadGachaHistory() {
        val newPlayerGachaHistory = mutableListOf<GachaLogBean.ListBean>()
        val permanentGachaHistory = mutableListOf<GachaLogBean.ListBean>()
        val characterGachaHistory = mutableListOf<GachaLogBean.ListBean>()
        val weaponGachaHistory = mutableListOf<GachaLogBean.ListBean>()

        thread {
            GachaType.gachaList.forEach {
                var page = 1
                var gachaLog:GachaLogBean? = null
                do {
                    val url = MiHoYoApi.getGachaLogUrl(logUrl,it,page,6,gachaLog?.list?.last()?.id?:"0")
                    Ok.getGachaHistory(url){
                        gachaLog = GSON.fromJson(it.optString("data"),GachaLogBean::class.java)
                    }

                    gachaLog!!.list.forEach {
                        when(it.gacha_type.toInt()){
                            GachaType.PERMANENT-> permanentGachaHistory+=it
                            GachaType.CHARACTER_ACTIVITY_01,GachaType.CHARACTER_ACTIVITY_02->characterGachaHistory+=it
                            GachaType.NEW_PLAYERS->newPlayerGachaHistory+=it
                            GachaType.WEAPON->weaponGachaHistory+=it
                        }
                    }
                    page++
                    //每加载10页休息5秒
                    if(page%10==0){
                        Thread.sleep(5000L)
                    }else{
                        Thread.sleep(AppConfig.LOAD_GACHA_HISTORY_INTERVAL)
                    }
                }while(gachaLog!!.list.size>0)

                runOnUiThread {
                    "page=$page".show()
                    when(it){
                        GachaType.PERMANENT-> bind.permanentOk.show()
                        GachaType.CHARACTER_ACTIVITY_01,GachaType.CHARACTER_ACTIVITY_02->bind.characterOk.show()
                        GachaType.NEW_PLAYERS->bind.newPlayerOk.show()
                        GachaType.WEAPON->bind.weaponOk.show()
                    }
                }
            }
            runOnUiThread {
                "加载完成".show()
                setResult(ActivityResponseCode.OK)
                finish()
            }
        }
    }

    //设置加载动画
    private fun setLoadingAnimation() {
        val loadingWidth = PaiMonsNoteBook.context.resources.displayMetrics.widthPixels - 108.dp

        val animGo = ObjectAnimator.ofFloat(bind.loading.iconKlee,"translationX",10f,loadingWidth)
        animGo.duration = 1500L

        val animBack = ObjectAnimator.ofFloat(bind.loading.iconKlee,"translationX",loadingWidth,10f)
        animBack.duration = 1500L

        val rotateBack =  ObjectAnimator.ofFloat(bind.loading.iconKlee,"rotationY",0f,180f)
        rotateBack.duration = 500L

        val rotateGo =  ObjectAnimator.ofFloat(bind.loading.iconKlee,"rotationY",180f,0f)
        rotateGo.duration = 500L

        val animSet = AnimatorSet()
        animSet.play(animGo).before(rotateBack)
        animSet.play(rotateBack).before(animBack)
        animSet.play(animBack).before(rotateGo)
        animSet.start()

        animSet.addListener(AnimatorFinished{
            if(isLoading){
                animSet.start()
            }
        })
    }

    override fun onBackPressed() {
        "请耐心等待加载完成~".show()
    }

    override fun onDestroy() {
        super.onDestroy()
        isLoading = false
    }

}