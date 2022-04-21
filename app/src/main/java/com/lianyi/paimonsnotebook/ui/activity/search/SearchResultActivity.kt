package com.lianyi.paimonsnotebook.ui.activity.search

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.bean.CharacterBean
import com.lianyi.paimonsnotebook.bean.PlayerCharacterInformationBean
import com.lianyi.paimonsnotebook.bean.PlayerInformationBean
import com.lianyi.paimonsnotebook.bean.SpiralAbyssBean
import com.lianyi.paimonsnotebook.databinding.*
import com.lianyi.paimonsnotebook.lib.adapter.PagerAdapter
import com.lianyi.paimonsnotebook.lib.adapter.ReAdapter
import com.lianyi.paimonsnotebook.lib.base.BaseActivity
import com.lianyi.paimonsnotebook.lib.information.Element
import com.lianyi.paimonsnotebook.lib.information.Format
import com.lianyi.paimonsnotebook.lib.information.MiHoYoApi
import com.lianyi.paimonsnotebook.lib.information.Star
import com.lianyi.paimonsnotebook.util.*

class SearchResultActivity : BaseActivity() {

    companion object{
        lateinit var playerInfo: PlayerInformationBean
    }

    lateinit var bind:ActivitySearchResultBinding
    private val titles = listOf("基本信息","角色信息","深境螺旋")
    private val pagers = mutableListOf<View>()
    private lateinit var roleId:String
    private lateinit var server:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(bind.root)

        pagers += layoutInflater.inflate(R.layout.pager_search_basic,null)
        pagers += layoutInflater.inflate(R.layout.pager_search_character,null)
        pagers += layoutInflater.inflate(R.layout.pager_search_abyss,null)

        bind.viewPager.adapter = PagerAdapter(pagers,titles)
        bind.tabLayout.setupWithViewPager(bind.viewPager)

        bind.viewPager.offscreenPageLimit = 3

        initData()
        setContentMargin(bind.root)
    }

    private fun initData(){
        roleId = intent.getStringExtra("roleId")?:""
        server = intent.getStringExtra("server")?:""

        loadBasicInformation()
        loadCharacter()
        loadAbyss()
    }

    private fun loadBasicInformation(){
        val page = PagerSearchBasicBinding.bind(pagers[0])

        page.activeDayNumber.infoName.text = "活跃天数"
        page.activeDayNumber.infoValue.text = playerInfo.stats.active_day_number.toString()
        page.achievementNumber.infoName.text = "成就达成数"
        page.achievementNumber.infoValue.text = playerInfo.stats.achievement_number.toString()
        page.avatarNumber.infoName.text = "获得角色数"
        page.avatarNumber.infoValue.text = playerInfo.stats.avatar_number.toString()
        page.spiralAbyss.infoName.text = "深境螺旋"
        page.spiralAbyss.infoValue.text = playerInfo.stats.spiral_abyss
        page.anemoculusNumber.infoName.text = "风神曈"
        page.anemoculusNumber.infoValue.text = playerInfo.stats.anemoculus_number.toString()
        page.geoculusNumber.infoName.text = "岩神瞳"
        page.geoculusNumber.infoValue.text = playerInfo.stats.geoculus_number.toString()
        page.electroculusNumber.infoName.text = "雷神瞳"
        page.electroculusNumber.infoValue.text = playerInfo.stats.electroculus_number.toString()
        page.wayPointNumber.infoName.text = "解锁传送点"
        page.wayPointNumber.infoValue.text = playerInfo.stats.way_point_number.toString()
        page.domainNumber.infoName.text = "解锁秘境"
        page.domainNumber.infoValue.text = playerInfo.stats.domain_number.toString()
        page.luxuriousChestNumber.infoName.text = "华丽宝箱数"
        page.luxuriousChestNumber.infoValue.text = playerInfo.stats.luxurious_chest_number.toString()
        page.preciousChestNumber.infoName.text = "珍贵宝箱数"
        page.preciousChestNumber.infoValue.text = playerInfo.stats.precious_chest_number.toString()
        page.exquisiteChestNumber.infoName.text = "精致宝箱数"
        page.exquisiteChestNumber.infoValue.text = playerInfo.stats.exquisite_chest_number.toString()
        page.commonChestNumber.infoName.text = "普通宝箱数"
        page.commonChestNumber.infoValue.text = playerInfo.stats.common_chest_number.toString()
        page.magicChestNumber.infoName.text = "奇馈宝箱数"
        page.magicChestNumber.infoValue.text = playerInfo.stats.magic_chest_number.toString()

        page.worldExplorationsList.adapter = ReAdapter(playerInfo.world_explorations,R.layout.item_world_explorations){
            view, worldExplorationsBean, position ->
            val item = ItemWorldExplorationsBinding.bind(view)

            loadImage(item.icon,worldExplorationsBean.icon)

            item.name.text = worldExplorationsBean.name

            //龙脊雪山地区的level 和供奉等级共享 会出现声望等级 故排除
            if(worldExplorationsBean.level>0 &&worldExplorationsBean.id!=3 ){
                item.reputationLevel.text = "${worldExplorationsBean.level}级"
            }else{
                item.reputationSpan.gone()
            }

            repeat(worldExplorationsBean.offerings.size){
                val text = TextView(item.root.context)
                text.text = "${worldExplorationsBean.offerings[it].name} -  ${worldExplorationsBean.offerings[it].level}级"
                text.textSize = 17f
                text.setTextColor(ContextCompat.getColor(bind.root.context,R.color.black))
                item.areaInformationSpan.addView(text)
            }

            item.explorationsValue.text = "${(worldExplorationsBean.exploration_percentage / 10f)}%"
        }

        page.homeLevel.infoName.text = "信任等阶"
        page.comfortNum.infoName.text = "最高洞天仙力"
        page.itemNum.infoName.text = "获得摆设数"
        page.visitNum.infoName.text = "历史访客数"

        if(playerInfo.homes.size>0){
            page.homeLevel.infoValue.text = playerInfo.homes.first().level.toString()
            page.comfortNum.infoValue.text = playerInfo.homes.first().comfort_num.toString()
            page.itemNum.infoValue.text = playerInfo.homes.first().item_num.toString()
            page.visitNum.infoValue.text = playerInfo.homes.first().visit_num.toString()
        }else{
            page.homeLevel.infoValue.text = "0"
            page.comfortNum.infoValue.text = "0"
            page.itemNum.infoValue.text = "0"
            page.visitNum.infoValue.text = "0"
        }

        page.unlockHomeList.adapter = ReAdapter(playerInfo.homes,R.layout.item_unlock_home){
            view, homesBean, position ->
            val item = ItemUnlockHomeBinding.bind(view)
            loadImage(item.icon,homesBean.icon)
            item.name.text = homesBean.name
        }

        setViewMarginBottomByNavigationBarHeight(page.homeSpan)
    }

    private fun loadCharacter(){
        val page = PagerSearchCharacterBinding.bind(pagers[1])

        MiHoYoApi.getCharacterData(playerInfo,roleId,server){
            setCharacterListAdapter(page, it.avatars)
        }

        page.apply {
            exportCharacterList.setOnClickListener {
                val w = list.width
                val h = list.height
                val size = list.childCount
                val paint = Paint()
                list.forEachIndexed { index, view ->
                    val holder = list.adapter?.createViewHolder(list,list.adapter?.getItemViewType(index)?:0)
                    list.adapter?.onBindViewHolder(holder!!,index)
                    holder?.itemView?.let {
                        it.measure(View.MeasureSpec.makeMeasureSpec(list.width,list.height),View.MeasureSpec.EXACTLY)
                        it.layout(0,0,it.measuredWidth,it.measuredHeight)
                    }
                }
            }
        }
        setViewMarginBottomByNavigationBarHeight(page.list)
    }

    private fun setCharacterListAdapter(page:PagerSearchCharacterBinding,avatars:List<PlayerCharacterInformationBean.AvatarsBean>){
        runOnUiThread {
            page.list.adapter = ReAdapter(avatars,R.layout.item_character){
                    view, avatarsBean, position ->
                val item = ItemCharacterBinding.bind(view)
                item.name.text = avatarsBean.name

                val character = CharacterBean.getCharacterByName(avatarsBean.name)

                if(character!=null){
                    item.starBackground.setImageResource(Star.getStarResourcesByStarNum(character.star,false))
                    item.type.setImageResource(Element.getImageResourceByType(character.element))
                    loadImage(item.icon,character.icon)
                }else{
                    loadImage(item.icon,avatarsBean.icon)
                }

                if(avatarsBean.actived_constellation_num>0){
                    item.activatedConstellationNum.show()
                    item.activatedConstellationNum.text = avatarsBean.actived_constellation_num.toString()
                }else{
                    item.activatedConstellationNum.gone()
                }

                item.value.text = avatarsBean.level.toString()
                item.name.text = avatarsBean.name

                item.root.setOnClickListener {
                    SearchResultCharacterDetailActivity.avatarsBean = avatarsBean
                    SearchResultCharacterDetailActivity.avatarsList = avatars
                    goA<SearchResultCharacterDetailActivity>()
                }
            }
        }
    }

    private fun loadAbyss(){
        val page = PagerSearchAbyssBinding.bind(pagers[2])

        MiHoYoApi.getAbyssData(roleId,server){
            setAbyssListAdapter(page,it)
        }

        setViewMarginBottomByNavigationBarHeight(page.list)
    }

    private fun setAbyssListAdapter(page:PagerSearchAbyssBinding,spiralAbyss:SpiralAbyssBean){
        runOnUiThread {
            page.maxFloor.infoName.text = "最深抵达"
            page.maxFloor.infoValue.text = spiralAbyss.max_floor
            page.totalBattleTimes.infoName.text = "战斗次数"
            page.totalBattleTimes.infoValue.text = spiralAbyss.total_battle_times.toString()
            page.totalStar.infoName.text = "渊星"
            page.totalStar.infoValue.text = spiralAbyss.total_star.toString()

            //战斗次数排行榜
            page.revealRankList.adapter = ReAdapter(spiralAbyss.reveal_rank,R.layout.item_character){
                    view, revealRankBean, position ->
                val item = ItemCharacterBinding.bind(view)
                item.valueName.gone()
                item.value.text = "${revealRankBean.value}次"
                item.starBackground.setBackgroundResource(Star.getStarResourcesByStarNum(revealRankBean.rarity,false))
                loadImage(item.icon,revealRankBean.avatar_icon)
                item.type.gone()
            }

            if(spiralAbyss.defeat_rank.size>0){
                loadImage(page.defeatRankIcon,spiralAbyss.defeat_rank.first().avatar_icon)
                page.defeatRankValue.text = spiralAbyss.defeat_rank.first().value.toString()
            }

            if(spiralAbyss.damage_rank.size>0){
                loadImage(page.damageRankIcon,spiralAbyss.damage_rank.first().avatar_icon)
                page.damageRankValue.text = spiralAbyss.damage_rank.first().value.toString()
            }

            if(spiralAbyss.take_damage_rank.size>0){
                loadImage(page.takeDamageRankIcon,spiralAbyss.take_damage_rank.first().avatar_icon)
                page.takeDamageRankValue.text = spiralAbyss.take_damage_rank.first().value.toString()
            }

            if(spiralAbyss.normal_skill_rank.size>0){
                loadImage(page.normalSkillRankIcon,spiralAbyss.normal_skill_rank.first().avatar_icon)
                page.normalSkillRankValue.text = spiralAbyss.normal_skill_rank.first().value.toString()
            }

            if(spiralAbyss.energy_skill_rank.size>0){
                loadImage(page.energySkillRankIcon,spiralAbyss.energy_skill_rank.first().avatar_icon)
                page.energySkillRankValue.text = spiralAbyss.energy_skill_rank.first().value.toString()
            }

            page.list.adapter = ReAdapter(spiralAbyss.floors,R.layout.item_abyss_tower){
                    view, floorsBean, position ->
                val tower = ItemAbyssTowerBinding.bind(view)
                tower.floor.text = floorsBean.index.toString()
                tower.star.text = floorsBean.star.toString()
                tower.maxStar.text = floorsBean.max_star.toString()


                tower.showContent.select {
                    if(it){
                        tower.list.measure(0,0)
                        openAndCloseAnimationVer(tower.floorSpan,40.dp.toInt(),tower.list.measuredHeight+40.dp.toInt(),500)
                        tower.dropDown.rotation = 0f
                    }else{
                        openAndCloseAnimationVer(tower.floorSpan,tower.list.measuredHeight+40.dp.toInt(),40.dp.toInt(),500)
                        tower.dropDown.rotation = 180f
                    }
                    tower.dropDown.animate().rotationBy(180f).duration = 500
                }

                tower.list.adapter = ReAdapter(floorsBean.levels,R.layout.item_abyss_floor){
                        view, levelsBean, position ->
                    val floor = ItemAbyssFloorBinding.bind(view)
                    floor.index.text = levelsBean.index.toString()

                    floor.time.text = Format.TIME_FULL.format(levelsBean.battles.first().timestamp.toLong()*1000L)

                    floor.characterList.adapter = ReAdapter(levelsBean.battles,R.layout.item_grid_layout_list){
                            view, battlesBean, position ->
                        val battles = ItemGridLayoutListBinding.bind(view)
                        battles.list.adapter = ReAdapter(battlesBean.avatars,R.layout.item_character_small){
                                view, avatarsBean, position ->
                            val character = ItemCharacterSmallBinding.bind(view)
                            character.starBackground.setImageResource(Star.getStarResourcesByStarNum(avatarsBean.rarity,false))
                            loadImage(character.icon,avatarsBean.icon)
                            character.value.text = avatarsBean.level.toString()
                        }
                    }

                    when(levelsBean.star){
                        1->{
                            floor.abyssStar1.alpha = 1f
                        }
                        2->{
                            floor.abyssStar1.alpha = 1f
                            floor.abyssStar2.alpha = 1f
                        }
                        3->{
                            floor.abyssStar1.alpha = 1f
                            floor.abyssStar2.alpha = 1f
                            floor.abyssStar3.alpha = 1f
                        }
                        else->{
                            floor.abyssStar1.alpha = 0.5f
                            floor.abyssStar2.alpha = 0.5f
                            floor.abyssStar3.alpha = 0.5f
                        }
                    }
                }
            }
        }
    }

    private fun exportImage(){

    }

}