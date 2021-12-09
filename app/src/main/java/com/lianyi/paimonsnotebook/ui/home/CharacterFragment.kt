package com.lianyi.paimonsnotebook.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.lianyi.paimonsnotebook.R
import com.lianyi.paimonsnotebook.activity.CharacterDetailActivity
import com.lianyi.paimonsnotebook.bean.EntityJsonBean
import com.lianyi.paimonsnotebook.config.CharacterProperty
import com.lianyi.paimonsnotebook.config.JsonCacheName
import com.lianyi.paimonsnotebook.config.WeaponType
import com.lianyi.paimonsnotebook.databinding.FragmentCharacterBinding
import com.lianyi.paimonsnotebook.databinding.ItemEntityBinding
import com.lianyi.paimonsnotebook.ui.RefreshData
import com.lianyi.paimonsnotebook.util.*
import me.jessyan.autosize.internal.CustomAdapt
import org.json.JSONArray

class CharacterFragment : Fragment(R.layout.fragment_character), CustomAdapt {

    lateinit var bind: FragmentCharacterBinding
    private val characterList = mutableListOf<EntityJsonBean>()
    private val characterShowList = mutableListOf<EntityJsonBean>()
    //选择的属性和装备类型
    private val selectProperty = mutableListOf<Int>()
    private val selectEquipType = mutableListOf<Int>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind = FragmentCharacterBinding.bind(view)

        val characterJsonArray = JSONArray(csp.getString(JsonCacheName.CHARACTER_LIST,"")!!)

        characterJsonArray.toList(characterList)
        characterList.forEach {
            characterShowList += it
        }

        //根据星级排序
        characterShowList.sortByDescending { it.star }

        bind.list.adapter = ReAdapter(characterShowList,R.layout.item_entity){
                view, entityJsonBean, position ->
            val item = ItemEntityBinding.bind(view)
            item.name.text = entityJsonBean.entity.name
            loadImage(item.icon,entityJsonBean.entity.iconUrl)
            item.type.setImageResource(CharacterProperty.getImageResourceByType(entityJsonBean.entityType))
            item.root.setOnClickListener {
                CharacterDetailActivity.detailInformation = entityJsonBean
                goA<CharacterDetailActivity>()
                activity?.overridePendingTransition(R.anim.activity_fade_in,R.anim.activity_fade_out)
            }
        }

        initSelect()

    }

    private fun notificationUpdate(){
        characterShowList.clear()
        characterList.forEach {  entity->
            selectProperty.forEach { property->
                if(entity.entityType==property)
                    characterShowList += entity
            }
        }
        val deleteList = mutableListOf<EntityJsonBean>()
        characterShowList.forEach { entity->
            if(selectEquipType.indexOf(entity.equipType)==-1){
                deleteList += entity
            }
        }
        deleteList.forEach {
            characterShowList.remove(it)
        }
        characterShowList.sortByDescending { it.star }
        activity?.runOnUiThread {
            bind.list.adapter?.notifyDataSetChanged()
            bind.selectSpan.isEnabled = true
        }
    }

    override fun isBaseOnWidth(): Boolean {
        return false
    }

    override fun getSizeInDp(): Float {
        return 730f
    }

    fun initSelect(){

        with(bind){

            selectProperty += CharacterProperty.FIRE
            selectProperty += CharacterProperty.WATER
            selectProperty += CharacterProperty.GRASS
            selectProperty += CharacterProperty.ICE
            selectProperty += CharacterProperty.WIND
            selectProperty += CharacterProperty.ELECT
            selectProperty += CharacterProperty.ROCK

            selectEquipType += WeaponType.ONE_HAND_SWORD
            selectEquipType += WeaponType.BOTH_HAND_SWORD
            selectEquipType += WeaponType.BOW_AND_ARROW
            selectEquipType += WeaponType.MAGIC_ARTS
            selectEquipType += WeaponType.SPEAR


            selectFire.select {
                if(it)
                    selectProperty.add(CharacterProperty.FIRE)
                else
                    selectProperty.remove(CharacterProperty.FIRE)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectWater.select {
                if(it)
                    selectProperty.add(CharacterProperty.WATER)
                else
                    selectProperty.remove(CharacterProperty.WATER)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectGrass.select {
                if(it)
                    selectProperty.add(CharacterProperty.GRASS)
                else
                    selectProperty.remove(CharacterProperty.GRASS)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectIce.select {
                if(it)
                    selectProperty.add(CharacterProperty.ICE)
                else
                    selectProperty.remove(CharacterProperty.ICE)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectWind.select {
                if(it)
                    selectProperty.add(CharacterProperty.WIND)
                else
                    selectProperty.remove(CharacterProperty.WIND)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectElect.select {
                if(it)
                    selectProperty.add(CharacterProperty.ELECT)
                else
                    selectProperty.remove(CharacterProperty.ELECT)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectRock.select {
                if(it)
                    selectProperty.add(CharacterProperty.ROCK)
                else
                    selectProperty.remove(CharacterProperty.ROCK)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }

            selectTypeOneHandSword.select {
                if(it)
                    selectEquipType.add(WeaponType.ONE_HAND_SWORD)
                else
                    selectEquipType.remove(WeaponType.ONE_HAND_SWORD)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectBothHandSword.select {
                if(it)
                    selectEquipType.add(WeaponType.BOTH_HAND_SWORD)
                else
                    selectEquipType.remove(WeaponType.BOTH_HAND_SWORD)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectBowAndArrow.select {
                if(it)
                    selectEquipType.add(WeaponType.BOW_AND_ARROW)
                else
                    selectEquipType.remove(WeaponType.BOW_AND_ARROW)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectMagicArts.select {
                if(it)
                    selectEquipType.add(WeaponType.MAGIC_ARTS)
                else
                    selectEquipType.remove(WeaponType.MAGIC_ARTS)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }
            selectSpear.select {
                if(it)
                    selectEquipType.add(WeaponType.SPEAR)
                else
                    selectEquipType.remove(WeaponType.SPEAR)
                bind.selectSpan.isEnabled = false
                notificationUpdate()
            }

        }
    }

}