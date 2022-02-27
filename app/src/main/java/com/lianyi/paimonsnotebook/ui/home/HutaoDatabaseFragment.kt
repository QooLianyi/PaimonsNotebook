package com.lianyi.paimonsnotebook.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.PlayerInformationBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.HutaoDatabaseUploadBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.HutaoLoginBean
import com.lianyi.paimonsnotebook.bean.hutaoapi.OverViewBean
import com.lianyi.paimonsnotebook.databinding.FragmentHutaoDatabaseBinding
import com.lianyi.paimonsnotebook.lib.adapter.MainViewPager2Adapter
import com.lianyi.paimonsnotebook.lib.adapter.NavigationViewSetupWithViewPager2
import com.lianyi.paimonsnotebook.lib.base.BaseFragment
import com.lianyi.paimonsnotebook.lib.information.HuTaoApi
import com.lianyi.paimonsnotebook.lib.information.JsonCacheName
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.lib.information.PagerIndex
import com.lianyi.paimonsnotebook.ui.hutao.*
import com.lianyi.paimonsnotebook.util.*
import kotlin.concurrent.thread

class HutaoDatabaseFragment : BaseFragment(R.layout.fragment_hutao_database) {
    lateinit var bind:FragmentHutaoDatabaseBinding

    private var initCount = 0
    private val successCount = 3

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentHutaoDatabaseBinding.bind(view)

        loginAndInit()
    }

    //重新获取胡桃API TOKEN 并重新获取数据
    private fun loginAndInit(){
        Ok.hutaoLogin {
            if(it.ok){
                sp.edit().apply{
                    putString(HuTaoApi.SP_TOKEN,GSON.fromJson(it.optString("data"),HutaoLoginBean::class.java).accessToken)
                    apply()
                }

                Ok.hutaoGet(HuTaoApi.GET_AVATARS){
                    if(it.ok){
                        csp.edit().apply{
                            putString(JsonCacheName.HUTAO_AVATARS_LIST,it.optString("data"))
                            apply()
                        }
                        checkStatus()
                    }
                }

                Ok.hutaoGet(HuTaoApi.GET_WEAPONS){
                    if(it.ok){
                        wsp.edit().apply {
                            putString(JsonCacheName.HUTAO_WEAPON_LIST,it.optString("data"))
                            apply()
                        }
                        checkStatus()
                    }
                }
                Ok.hutaoGet(HuTaoApi.GET_RELIQUARIES){
                    if(it.ok){
                        sp.edit().apply {
                            putString(JsonCacheName.HUTAO_RELIQUARIES_LIST,it.optString("data"))
                            apply()
                        }
                        checkStatus()
                    }
                }
                Ok.hutaoGet(HuTaoApi.OVER_VIEW){
                    if(it.ok){
                        val overView = GSON.fromJson(it.optString("data"), OverViewBean::class.java)
                        activity?.runOnUiThread {
                            bind.collectedPlayerCount.text = overView.collectedPlayerCount.toString()
                            bind.fullStarPlayerCount.text = overView.fullStarPlayerCount.toString()
                        }
                    }
                }
            }else{
                activity?.runOnUiThread {
                    "胡桃数据库请求失败:登录失败".show()
                }
            }
        }
    }

    private fun checkStatus(){
        initCount++
        if(initCount==successCount){
            initView()
        }
    }

    private fun initView() {
        activity?.runOnUiThread {
            bind.hutaoMenu.setOnClickListener {
                bind.hutaoContainer.openDrawer(GravityCompat.START)
            }
            val fragments = mutableMapOf(
                PagerIndex.HUTAO_AVATAR_PARTICIPATION to AvatarParticipationFragment(),
                PagerIndex.HUTAO_CONSTELLATION to ConstellationFragment(),
                PagerIndex.HUTAO_TEAM_COLLOCATION to TeamCollocationFragment(),
                PagerIndex.HUTAO_WEAPON_USAGE to WeaponUsageFragment(),
                PagerIndex.HUTAO_AVATAR_RELIQUARY_USAGE to AvatarReliquaryUsageFragment(),
                PagerIndex.HUTAO_TEAM_COMBINATION to TeamCombinationFragment()
            )
            bind.viewPager2.adapter = MainViewPager2Adapter(fragments,fragmentManager!!,lifecycle)
//            bind.viewPager2.offscreenPageLimit = 6
            NavigationViewSetupWithViewPager2(bind.viewPager2,bind.navView){
                navigationView, viewPager2 ->
                viewPager2.isUserInputEnabled = false
            }.attach()

            setViewMarginBottomByNavigationBarHeight(bind.overViewSpan)

            bind.upload.setOnClickListener {
                //关闭侧边栏
                bind.hutaoContainer.closeDrawer(GravityCompat.START)
                val content = """
                    接下来将要上传默认用户(${mainUser?.gameUid})的数据
                    将要上传的数据为:
                        用户的游戏 UID
                        用户的深境螺旋记录
                        用户的角色信息及其装备的武器和圣遗物信息
                        
                    接下来是要继续上传吗?
                """.trimIndent()
                showConfirmAlertDialog(activity!!,"提示",content){
                    if(it){
                        showLoading(activity!!)
                        upLoadData()
                    }
                }
            }
        }
    }

    private fun upLoadData(){
        thread {
            getPlayerInformation{ characterList: MutableList<HutaoDatabaseUploadBean.PlayerAvatarsBean>, abyssList: MutableList<HutaoDatabaseUploadBean.PlayerSpiralAbyssesLevelsBean> ->
                val record = HutaoDatabaseUploadBean(
                    mainUser!!.gameUid,
                    characterList,
                    abyssList
                )

                Ok.hutaoPost(HuTaoApi.RECORD_UPLOAD, GSON.toJson(record).toMyRequestBody()){
                    activity?.runOnUiThread {
                        if(it.ok){
                            showSuccessInformationAlertDialog(activity!!,"上传成功","感谢您对胡桃数据库的支持(上传的数据每2小时刷新一次)")
                        }else{
                            showFailureAlertDialog(activity!!,"上传失败","${it.optString("retcode")}:${it.optString("message")}")
                        }
                        dismissLoadingWindow()
                    }
                }
            }
        }
    }

    private fun getPlayerInformation(block:(MutableList<HutaoDatabaseUploadBean.PlayerAvatarsBean>,MutableList<HutaoDatabaseUploadBean.PlayerSpiralAbyssesLevelsBean>)->Unit){
        MiHoYoApi.getPlayerData(mainUser!!.gameUid){ b: Boolean, playerInformationBean: PlayerInformationBean?, intent: Intent? ->
            if(b){
                val roleId = intent?.getStringExtra("roleId")?:""
                val server = intent?.getStringExtra("server")?:""
                getPlayerAvatars(playerInformationBean!!,roleId,server){ characterList->
                    getPlayerSpiralAbyssesLevels(roleId,server){ abyssList ->
                        block(characterList,abyssList)
                    }
                }
            }else{
                activity?.runOnUiThread {
                    showFailureAlertDialog(activity!!,"上传失败","可能是网络错误导致的,如果一直出现该提示请尝试联系开发者解决。")
                }
            }
        }
    }

    private fun getPlayerAvatars(playerInformationBean: PlayerInformationBean,roleId:String,server:String,block:(MutableList<HutaoDatabaseUploadBean.PlayerAvatarsBean>)->Unit){
        val characterList = mutableListOf<HutaoDatabaseUploadBean.PlayerAvatarsBean>()
        MiHoYoApi.getCharacterData(playerInformationBean,roleId,server){
            it.avatars.forEach {
                val weapon = HutaoDatabaseUploadBean.PlayerAvatarsBean.WeaponBean(it.weapon.id,it.weapon.level,it.weapon.affix_level)
                val reliquarySets = mutableListOf<HutaoDatabaseUploadBean.PlayerAvatarsBean.ReliquarySetsBean>()

                it.reliquaries.groupBy { it.set.id }.toList().forEach {
                    if(it.second.size>2){
                        reliquarySets += HutaoDatabaseUploadBean.PlayerAvatarsBean.ReliquarySetsBean(it.second.first().set.id,it.second.size)
                    }
                }
                characterList += HutaoDatabaseUploadBean.PlayerAvatarsBean(it.id,
                    it.level,
                    it.actived_constellation_num,
                    weapon,
                    reliquarySets
                )
            }
            block(characterList)
        }
    }

    private fun getPlayerSpiralAbyssesLevels(roleId:String,server:String,block:(MutableList<HutaoDatabaseUploadBean.PlayerSpiralAbyssesLevelsBean>)->Unit){
        val abyssList = mutableListOf<HutaoDatabaseUploadBean.PlayerSpiralAbyssesLevelsBean>()
        MiHoYoApi.getAbyssData(roleId,server){
            it.floors.forEach { floor->
                floor.levels.forEach { level->

                    val battleList = mutableListOf<HutaoDatabaseUploadBean.PlayerSpiralAbyssesLevelsBean.BattlesBean>()
                    level.battles.forEach { battle->

                        val characterIds = mutableListOf<Int>()
                        battle.avatars.forEach {avatar->
                            characterIds += avatar.id
                        }
                        battleList += HutaoDatabaseUploadBean.PlayerSpiralAbyssesLevelsBean.BattlesBean(battle.index,characterIds)
                    }
                    abyssList += HutaoDatabaseUploadBean.PlayerSpiralAbyssesLevelsBean(
                        floor.index,
                        level.index,
                        level.star,
                        battleList
                    )
                }
            }
            block(abyssList)
        }
    }
}