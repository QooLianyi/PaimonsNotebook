package com.lianyi.paimonsnotebook.ui.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.bean.gacha.GachaLogBean
import com.lianyi.paimonsnotebook.bean.gacha.UIGFExcelBean
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

        initView()
        loadGachaHistory()
    }

    private fun initView() {
        setLoadingAnimation()
        setContentMargin(bind.root)
    }

    private fun loadGachaHistory() {
        thread {
            val list = mutableListOf<UIGFExcelBean>()

            GachaType.gachaList.forEach { gachaType->
                var page = 1
                var gachaLog: GachaLogBean? = null
                var lastId = ""
                var continueRead = true

                runOnUiThread {
                    bind.gachaName.text = GachaType.getNameByType(gachaType)
                }
                do {
                    runOnUiThread {
                        bind.page.text = "$page"
                    }

                    val url = MiHoYoApi.getGachaLogUrl(logUrl,gachaType,page,6,gachaLog?.list?.last()?.id?:"0")
                    Ok.getSync(url){
                        gachaLog = GSON.fromJson(it.optString("data"),GachaLogBean::class.java)
                    }

                    if(lastId.isEmpty()&&!gachaLog!!.list.isNullOrEmpty()){
                        lastId = PaiMonsNotebookDataBase.INSTANCE.getLastGachaHistoryId(gachaType.toString(),gachaLog!!.list.first().uid)
                        println("lastId == $lastId")
                    }

                    gachaLog!!.list.forEach { bean->
                        with(bean){
                            val uigfGachaBean =
                                UIGFExcelBean(
                                    count,
                                    gacha_type,
                                    id,
                                    item_id,
                                    item_type,
                                    lang,
                                    name,
                                    rank_type,
                                    time,
                                    uid,
                                    GachaType.getUIGFType(gacha_type)
                                )
                            if(uigfGachaBean.id!=lastId){
                                list += uigfGachaBean
                            }else{
                                continueRead = false
                            }
                        }
                    }

                    //每加载10页休息2~3秒
                    if(page%10==0){
                        Thread.sleep((2000L..3000L).random())
                    }else{
                        Thread.sleep(AppConfig.LOAD_GACHA_HISTORY_INTERVAL)
                    }
                    page++

                }while(gachaLog!!.list.size>0&&continueRead)

                runOnUiThread {
                    when(gachaType){
                        GachaType.PERMANENT-> bind.permanentOk.show()
                        GachaType.CHARACTER_ACTIVITY_01,GachaType.CHARACTER_ACTIVITY_02->bind.characterOk.show()
                        GachaType.NEW_PLAYERS->bind.newPlayerOk.show()
                        GachaType.WEAPON->bind.weaponOk.show()
                    }
                }
            }

            if(list.size>0){
                //写入数据库
                PaiMonsNotebookDataBase.INSTANCE.writeGachaHistoryForExcel(list.first().uid,list)
            }
            runOnUiThread {
                "加载完成".showLong()
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